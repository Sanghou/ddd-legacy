package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.domain.*;
import kitchenpos.infra.PurgomalumClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final PurgomalumClient purgomalumClient;

    public ProductService(
        final ProductRepository productRepository,
        final MenuRepository menuRepository,
        final PurgomalumClient purgomalumClient
    ) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.purgomalumClient = purgomalumClient;
    }

    @Transactional
    public Product create(final Product product) {
        if (purgomalumClient.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어 금지");
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product changePrice(final UUID productId, final BigDecimal price) {
        final Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchElementException::new);
        final List<Menu> menus = menuRepository.findAllByProductId(productId);

        product.updatePrice(price);
        menus.forEach(Menu::updateDisplay);

        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public boolean isAllRegistered(List<UUID> ids) {
        return productRepository.countAllByIds(ids) == ids.size();
    }
}
