package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    @Query("SELECT m FROM Menu m, MenuProduct mp WHERE mp.product.id = :productId")
    List<Menu> findAllByProductId(@Param("productId") UUID productId);

    @Query("SELECT m FROM Menu m WHERE m.id IN :ids")
    List<Menu> findAllByIds(List<UUID> ids);
}
