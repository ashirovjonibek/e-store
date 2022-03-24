package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Discount;

import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
     Page<Discount> findAllByDeleteFalse(Pageable pageable);
     Optional<Discount> findByIdAndDeleteFalse(UUID id);
}
