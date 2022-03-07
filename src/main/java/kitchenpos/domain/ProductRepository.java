package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("SELECT COUNT(p) FROM Product p WHERE p.id IN :ids")
  long countAllByIds(List<UUID> ids);
}
