package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threetenbp.ThreeTenBackPortJpaConverters;
import uz.e_store.entity.template.AbsEntity;
import uz.e_store.entity.template.AbsNameEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount extends AbsNameEntity {

    private float percent;

    @Column(name = "expiration_date",nullable = false)
    private Date expirationDate;

    private boolean delete;
}
