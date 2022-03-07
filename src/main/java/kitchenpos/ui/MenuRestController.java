package kitchenpos.ui;

import java.util.NoSuchElementException;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/menus")
@RestController
public class MenuRestController {
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    public MenuRestController(final MenuService menuService, final MenuGroupService menuGroupService) {
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        final Menu response = menuService.create(request.toEntity(menuGroup));

        return ResponseEntity.created(URI.create("/api/menus/" + response.getId()))
            .body(response);
    }

    @PutMapping("/{menuId}/price")
    public ResponseEntity<Menu> changePrice(@PathVariable final UUID menuId, @RequestBody final Menu request) {
        return ResponseEntity.ok(menuService.changePrice(menuId, request.getPrice()));
    }

    @PutMapping("/{menuId}/display")
    public ResponseEntity<Menu> display(@PathVariable final UUID menuId) {
        return ResponseEntity.ok(menuService.display(menuId));
    }

    @PutMapping("/{menuId}/hide")
    public ResponseEntity<Menu> hide(@PathVariable final UUID menuId) {
        return ResponseEntity.ok(menuService.hide(menuId));
    }

    @GetMapping
    public ResponseEntity<List<Menu>> findAll() {
        return ResponseEntity.ok(menuService.findAll());
    }
}
