package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.Entity;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Attachment extends AbsEntity {
    private String name;
    private String url;
    private String contentType;
    private String extension;
    private float size;
}
