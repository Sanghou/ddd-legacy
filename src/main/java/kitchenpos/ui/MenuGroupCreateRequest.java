package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
  private String name;


  public MenuGroup toEntity() {
    return new MenuGroup(name);
  }
}
