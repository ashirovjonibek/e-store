package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Contact;
import uz.e_store.entity.Order;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Page<Contact> findAllByUserId(UUID userId, Pageable pageable);

    @Override
    Page<Contact> findAll(Pageable pageable);
}
