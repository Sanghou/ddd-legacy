package kitchenpos.Mocker;

import java.util.UUID;
import kitchenpos.domain.MenuGroup;

public class MenuGroupMocker extends MenuGroup{

  public static MenuGroup createMenuGroup() {
    MenuGroup menuGroup = new MenuGroup();
    UUID uuid = UUID.randomUUID();
    menuGroup.setId(uuid);
    menuGroup.setName("Default");
    return menuGroup;
  }
}
