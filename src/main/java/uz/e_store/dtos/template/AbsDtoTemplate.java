package uz.e_store.dtos.template;

import lombok.Data;
import uz.e_store.dtos.response.CreatedByUpdatedByDto;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.UUID;
@Data
@MappedSuperclass
public abstract class AbsDtoTemplate {
    private UUID id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private CreatedByUpdatedByDto createdBy;

    private CreatedByUpdatedByDto updatedBy;

    private String description;

    private boolean active;
}
