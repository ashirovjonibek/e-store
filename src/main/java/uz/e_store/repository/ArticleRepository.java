package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Article;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Page<Article> findAllByDeleteFalse(Pageable pageable);
    Optional<Article> findByIdAndDeleteFalse(Integer id);
}
