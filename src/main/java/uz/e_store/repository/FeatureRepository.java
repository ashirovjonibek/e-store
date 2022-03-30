package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Features;

import java.util.List;
import java.util.UUID;

public interface FeatureRepository extends JpaRepository<Features, UUID> {
    List<Features> findAllByProductId(UUID id);
}
