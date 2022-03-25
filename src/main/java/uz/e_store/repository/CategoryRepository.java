package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.e_store.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAllByDeleteFalse(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndId(String name, Integer id);

    Optional<Category> findByIdAndDeleteFalse(Integer id);

    Page<Category> findAllByGenderIdAndSeasonIdAndNameContainingIgnoreCaseAndDeleteFalse(Integer genderId,Integer seasonId,String search, Pageable pageable);

    Page<Category> findAllByGenderIdAndSeasonIdAndDeleteFalse(Integer genderId,Integer seasonId, Pageable pageable);

    Page<Category> findAllByGenderIdAndNameContainingIgnoreCaseAndDeleteFalse(Integer genderId,String search, Pageable pageable);

    Page<Category> findAllBySeasonIdAndNameContainingIgnoreCaseAndDeleteFalse(Integer seasonId,String search, Pageable pageable);

    Page<Category> findAllBySeasonIdAndDeleteFalse(Integer seasonId, Pageable pageable);

    Page<Category> findAllByNameContainingIgnoreCaseAndDeleteFalse(String search, Pageable pageable);

    Page<Category> findAllByGenderIdAndDeleteFalse(Integer genderId, Pageable pageable);

}
