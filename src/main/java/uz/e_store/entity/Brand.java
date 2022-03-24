package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsNameEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand extends AbsNameEntity {
    @Column(unique = true)
    private String brandName;
}
