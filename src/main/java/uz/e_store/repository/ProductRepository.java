package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
