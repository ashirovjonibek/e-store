package uz.e_store.dtos.template;

import lombok.Data;
import uz.e_store.dtos.response.CreatedByUpdatedByDto;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public abstract class AbsDtoNameTemplate {
    private Integer id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private CreatedByUpdatedByDto createdBy;

    private CreatedByUpdatedByDto updatedBy;

    private String description;

    private boolean active;
}
