package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

  public static final MenuGroup DEFAULT_MENU_GROUP = 메뉴_그룹_생성();

  private static final String VALID_MENU_GROUP_NAME = "메뉴그룹";

  public static MenuGroup 메뉴_그룹_생성(String name) {
    return new MenuGroup(name);
  }

  public static MenuGroup 메뉴_그룹_생성() {
    return new MenuGroup(VALID_MENU_GROUP_NAME);
  }
}
