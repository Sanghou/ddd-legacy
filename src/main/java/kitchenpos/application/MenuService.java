package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.infra.PurgomalumClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final PurgomalumClient purgomalumClient;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository,
        final PurgomalumClient purgomalumClient
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.purgomalumClient = purgomalumClient;
    }
/*
    1. View 로직과 도메인 로직 분리
    2. 비지니스 로직에서 도메인 로직으로 이동
    => 일단 검증하고 싶은 것들을 리스트업하고
    비즈니스 로직인지 도메인 로직인지 먼저 판단
    => 맞는 범위에서 테스트를 만들어보자
    Fake Stub 만들어서 Mock 대체하는 방법 찾아보기

    => 금요일까지
    -> 어떤 부분이 도메인 로직 / 어떤 부분이 View 로직이 될 수 있는지 고민해보자
    -> View 로직 분리 DTO 관련 컨벤션 고민을 해보면 좋을 것 같음.
    -> 어떤 테스트를 짤지 문서 기반으로 리스트업
 */
    @Transactional
    public Menu create(Menu request) {
        final BigDecimal price = request.getPrice();
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(NoSuchElementException::new);
        final List<MenuProduct> menuProductRequests = request.getMenuProducts();
        if (Objects.isNull(menuProductRequests) || menuProductRequests.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<Product> products = productRepository.findAllById(
            menuProductRequests.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList())
        );
        if (products.size() != menuProductRequests.size()) {
            throw new IllegalArgumentException();
        }
        final List<MenuProduct> menuProducts = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProductRequest : menuProductRequests) {
            final long quantity = menuProductRequest.getQuantity();
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(NoSuchElementException::new);
            sum = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(quantity);
            menuProducts.add(menuProduct);
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        final String name = request.getName();
        if (Objects.isNull(name) || purgomalumClient.containsProfanity(name)) {
            throw new IllegalArgumentException();
        }
        final Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroup(menuGroup);
        menu.setDisplayed(request.isDisplayed());
        menu.setMenuProducts(menuProducts);
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu changePrice(final UUID menuId, final Menu request) {
        final BigDecimal price = request.getPrice();
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final BigDecimal sum = menuProduct.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            if (price.compareTo(sum) > 0) {
                throw new IllegalArgumentException();
            }
        }
//        BigDecimal sum = calculatePrice(menu);
//        if(price.compareTo(sum) > 0) {
//            throw new IllegalArgumentException();
//        }
        menu.setPrice(price);
        return menu;
    }

    private BigDecimal calculatePrice(Menu menu) {
        return menu.getMenuProducts()
            .stream()
            .map(menuProduct ->
                menuProduct.getProduct()
                    .getPrice()
                    .multiply(
                        BigDecimal.valueOf(menuProduct.getQuantity())
                    ))
            // 저 안을 menuProduct의 책임으로 보내면 더 좋은 코드가 됐겠다.
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMenuProductPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = menuProduct.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity()))
                .add(sum);
        }
        return sum;
    }

    @Transactional
    public Menu display(final UUID menuId) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);
        final BigDecimal sum = calculateMenuProductPrice(menu.getMenuProducts());
        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalStateException();
        }
        menu.setDisplayed(true);
        return menu;
    }

    @Transactional
    public Menu hide(final UUID menuId) {
        final Menu menu = menuRepository.findById(menuId)
            .orElseThrow(NoSuchElementException::new);
        menu.setDisplayed(false);
        return menu;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
