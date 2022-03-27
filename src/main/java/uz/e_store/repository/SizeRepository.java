package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Size;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    Page<Size> findAllByDeleteFalse(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndId(String name, Integer id);

    Optional<Size> findByIdAndDeleteFalse(Integer id);

    Page<Size> findAllByCategoryIdAndDeleteFalse(Integer categoryId, Pageable pageable);

    Page<Size> findAllByCategoryIdAndNameContainingIgnoreCaseAndDeleteFalse(Integer categoryId, String search, Pageable pageable);

    Page<Size> findAllByNameContainingIgnoreCaseAndDeleteFalse(String search, Pageable pageable);
}
