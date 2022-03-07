package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

  private String name;

  private BigDecimal price;

  private UUID menuGroupId;

  private boolean dislayed;

  private List<MenuProduct> menuProducts;

  public MenuCreateRequest() {

  }

  public MenuCreateRequest(String name, BigDecimal price, UUID menuGroupId, boolean dislayed, List<MenuProduct> menuProducts) {
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.dislayed = dislayed;
    this.menuProducts = menuProducts;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public UUID getMenuGroupId() {
    return menuGroupId;
  }

  public boolean isDislayed() {
    return dislayed;
  }

  public List<MenuProduct> getMenuProducts() {
    return menuProducts;
  }

  public Menu toEntity(MenuGroup menuGroup) {
    return new Menu(name, price, menuGroup, dislayed, menuProducts);
  }
}
