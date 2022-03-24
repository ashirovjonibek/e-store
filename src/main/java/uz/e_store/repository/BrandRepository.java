package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand,Integer> {
    Page<Brand> findAllByDeleteFalse(Pageable pageable);

    boolean existsByBrandName(String brandName);

    boolean existsByBrandNameAndId(String brandName,Integer id);
}
