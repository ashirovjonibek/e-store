package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.Entity;
@EqualsAndHashCode(callSuper = true)
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

    public Attachment(String name, String contentType, String extension, float size) {
        this.name = name;
        this.contentType = contentType;
        this.extension = extension;
        this.size = size;
    }
}
