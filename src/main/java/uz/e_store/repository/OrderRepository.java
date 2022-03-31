package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByProductIdAndUserId(UUID productId, UUID userId, Pageable pageable);

    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    Page<Order> findAllByProductId(UUID productId, Pageable pageable);
}
