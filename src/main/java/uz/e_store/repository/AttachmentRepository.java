package uz.e_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Attachment;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
