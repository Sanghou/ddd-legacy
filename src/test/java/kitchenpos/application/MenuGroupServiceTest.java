package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(Lifecycle.PER_CLASS)
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
    MenuGroup menuGroup = new MenuGroup();
    menuGroup.setName("비싼 음식 그룹");
    assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
  }

  /*
   TODO : 해당 테스트 확인 필요
   productRepo랑 동일
   menuGroupService.create 테스트가 필요 없나? 도메인에서 처리하니까
   */
  @Test
  void findAll_findAllProducts() {
    MenuGroup menuGroup1 = new MenuGroup();
    menuGroup1.setName("비싼 음식 그룹");
    MenuGroup menuGroup2 = new MenuGroup();
    menuGroup2.setName("싼 음식 그룹");
    menuGroupService.create(menuGroup1);
    menuGroupService.create(menuGroup2);
    List<MenuGroup> menuGroups = new ArrayList<>();
    menuGroups.add(menuGroup1);
    menuGroups.add(menuGroup2);

    given(menuGroupRepository.findAll()).willReturn(menuGroups);

    List<MenuGroup> menuGroupList = menuGroupService.findAll();
    assertThat(menuGroupList.size()).isEqualTo(2);
  }
}
