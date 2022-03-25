package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Season;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season,Integer> {
    Page<Season> findAllByDeleteFalse(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndId(String name, Integer id);

    Optional<Season> findByIdAndDeleteFalse(Integer id);
}
