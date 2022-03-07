package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

  private static final String VALID_MENUGROUP_NAME = "메뉴그룹";


  public static MenuGroup 메뉴그룹_생성(String name) {
    return new MenuGroup(name);
  }

  public static MenuGroup 메뉴그룹_생성() {
    return new MenuGroup(VALID_MENUGROUP_NAME);
  }
}
