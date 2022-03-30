package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
