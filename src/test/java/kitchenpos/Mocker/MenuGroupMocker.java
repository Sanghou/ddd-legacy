package kitchenpos.Mocker;

import kitchenpos.domain.MenuGroup;

public class MenuGroupMocker extends MenuGroup{

  public static MenuGroup createMenuGroup() {
    return new MenuGroup("Default");
  }
}
