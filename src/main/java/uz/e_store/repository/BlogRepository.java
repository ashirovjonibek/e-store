package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Blog;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog,Integer> {
    Page<Blog> findAllByDeleteFalse(Pageable pageable);

    Optional<Blog> findByIdAndDeleteFalse(Integer id);
}
