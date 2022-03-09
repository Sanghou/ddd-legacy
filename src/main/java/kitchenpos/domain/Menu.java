package kitchenpos.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Table(name = "menu")
@Entity
public class Menu {

  @Column(name = "id", columnDefinition = "varbinary(16)")
  @Id
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Min(0)
  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @ManyToOne(optional = false)
  @JoinColumn(
      name = "menu_group_id",
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_menu_to_menu_group")
  )
  private MenuGroup menuGroup;

  @Column(name = "displayed", nullable = false)
  private boolean displayed;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "menu_id",
      nullable = false,
      columnDefinition = "varbinary(16)",
      foreignKey = @ForeignKey(name = "fk_menu_product_to_menu")
  )
  private List<MenuProduct> menuProducts;

  @Transient
  private UUID menuGroupId;

  public Menu() {
  }

  public Menu(String name, BigDecimal price) {
    this(name, price, null, false, new ArrayList<>());
  }

  public Menu(String name, BigDecimal price, MenuGroup menuGroup, boolean displayed,
      List<MenuProduct> menuProducts) {
    verify(name, price, menuProducts);
    this.id = UUID.randomUUID();
    this.name = name;
    this.price = price;
    this.menuGroup = menuGroup;
    this.menuProducts = menuProducts;
  }

  private void verify(String name, BigDecimal price, List<MenuProduct> menuProducts) {
    verifyName(name);
    verifyPrice(price, menuProducts);
    verifyMenuProducts(menuProducts);
  }

  private void verifyName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("이름이 잘못됐어요!");
    }
  }

  private void verifyPrice(BigDecimal price, List<MenuProduct> menuProducts) {
    if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("음식 가격이 잘못됐어요!");
    }

    System.out.println(calculateMenuProductSum(menuProducts));
    if (price.compareTo(calculateMenuProductSum(menuProducts)) > 0) {
      throw new IllegalArgumentException();
    }
  }

  private void verifyMenuProducts(List<MenuProduct> menuProducts) {
    if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  // price <= menuProducts.sum()
  public void updatePrice(BigDecimal price) {
    verifyPrice(price, menuProducts);
    this.price = price;
  }

  public MenuGroup getMenuGroup() {
    return menuGroup;
  }

  public void setMenuGroup(final MenuGroup menuGroup) {
    this.menuGroup = menuGroup;
  }

  public boolean isDisplayed() {
    return displayed;
  }

  public void makeVisible() {
//    verifyPrice(price, menuProducts); -> 이미 생성시점에 검증되고 값을 업데이트할 때도 검증되기 때문에 이런 케이스는 존재하지 않음
    this.displayed = true;
  }
  
  public void makeInvisible() {
    this.displayed = false;
  }

  public List<MenuProduct> getMenuProducts() {
    return menuProducts;
  }

  public void setMenuProducts(final List<MenuProduct> menuProducts) {
    this.menuProducts = menuProducts;
  }

  public UUID getMenuGroupId() {
    return menuGroupId;
  }

  public void setMenuGroupId(final UUID menuGroupId) {
    this.menuGroupId = menuGroupId;
  }

  private BigDecimal calculatePrice() {
    return menuProducts
        .stream()
        .map(MenuProduct::price)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void updateDisplay() {
    if (price.compareTo(calculatePrice()) > 0) {
      this.displayed = false;
    }
  }

  public List<UUID> getProductIds() {
    return menuProducts.stream()
        .map(MenuProduct::getProductId)
        .collect(Collectors.toList());
  }

  /*
  일급 컬렉션으로 List를 분리해서 처리하는게 이론적으로 맞는 것 같음.
  너무 오버엔지니어링 같아서 분리하지 않고 여기서 사용함.
   */

  private BigDecimal calculateMenuProductSum(List<MenuProduct> menuProducts) {
    return menuProducts.stream()
        .map(MenuProduct::price)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
