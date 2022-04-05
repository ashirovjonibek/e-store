package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.BrandFeature;

import java.util.List;

public interface BrandFeatureRepository extends JpaRepository<BrandFeature, Integer> {
    List<BrandFeature> findAllByBrandId(Integer id);
}
