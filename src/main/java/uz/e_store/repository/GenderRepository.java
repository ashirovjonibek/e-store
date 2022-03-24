package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Gender;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender,Integer> {
    Page<Gender> findAllByDeleteFalse(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndId(String name, Integer id);

    Optional<Gender> findByIdAndDeleteFalse(Integer id);
}
