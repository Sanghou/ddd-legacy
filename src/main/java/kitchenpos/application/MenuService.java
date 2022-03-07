package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.infra.PurgomalumClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final PurgomalumClient purgomalumClient;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository,
        final ProductService productService,
        final PurgomalumClient purgomalumClient
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.purgomalumClient = purgomalumClient;
    }

    @Transactional
    public Menu create(Menu menu) {
        // TODO: function name
        if (!productService.isAllRegistered(menu.getProductIds())) {
            throw new IllegalArgumentException();
        }

        if (purgomalumClient.containsProfanity(menu.getName())) {
            throw new IllegalArgumentException();
        }
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu changePrice(final UUID menuId, final BigDecimal price) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);

        menu.updatePrice(price);

        return menu;
    }

    @Transactional
    public Menu display(final UUID menuId) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);
        menu.makeVisible();
        return menu;
    }

    @Transactional
    public Menu hide(final UUID menuId) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);
        menu.makeInvisible();
        return menu;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
