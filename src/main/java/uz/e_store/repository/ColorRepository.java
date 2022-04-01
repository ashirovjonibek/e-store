package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Color;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color,Integer> {
    Page<Color> findAllByDeleteFalse(Pageable pageable);

    boolean existsByColorHexAndDeleteFalse(String hex);

    boolean existsByColorHexAndIdAndDeleteFalse(String hex,Integer id);

    Optional<Color> findByIdAndDeleteFalse(Integer id);
}
