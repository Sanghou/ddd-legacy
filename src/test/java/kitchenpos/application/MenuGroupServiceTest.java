package kitchenpos.application;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

  @InjectMocks private MenuGroupService menuGroupService;
  @Mock private MenuGroupRepository menuGroupRepository;


  @BeforeEach
  void setUp() {
    menuGroupService = new MenuGroupService(menuGroupRepository);
  }

  @Test
  void create_validString_isTrue() {
    MenuGroup menuGroup = MenuGroupFixture.메뉴그룹_생성();

    menuGroup.updateName("비싼 음식 그룹");

    assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
  }

  /*
   productRepo랑 동일
   menuGroupService.create 테스트가 필요 없나? 도메인에서 처리하니까
   */
//  @Test
//  void findAll_findAllProducts() {
//    MenuGroup menuGroup1 = new MenuGroup();
//    menuGroup1.updateName("비싼 음식 그룹");
//    MenuGroup menuGroup2 = new MenuGroup();
//    menuGroup2.updateName("싼 음식 그룹");
//    menuGroupService.create(menuGroup1);
//    menuGroupService.create(menuGroup2);
//    List<MenuGroup> menuGroups = new ArrayList<>();
//    menuGroups.add(menuGroup1);
//    menuGroups.add(menuGroup2);
//
//    given(menuGroupRepository.findAll()).willReturn(menuGroups);
//
//    List<MenuGroup> menuGroupList = menuGroupService.findAll();
//    assertThat(menuGroupList.size()).isEqualTo(2);
//  }
}
