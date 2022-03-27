package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends AbsNameEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private Season season;

    @ManyToOne
    private Category parent;
}
