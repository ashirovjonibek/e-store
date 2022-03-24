package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount extends AbsEntity {
    private float percent;

    private Timestamp expirationDate;

    private boolean delete;
}
