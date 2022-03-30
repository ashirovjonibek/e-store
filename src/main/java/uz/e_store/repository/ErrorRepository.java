package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Error;

public interface ErrorRepository extends JpaRepository<Error, Long> {
}
