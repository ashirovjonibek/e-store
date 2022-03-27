package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends AbsEntity {

    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> attachments;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    private Size size;

    @ManyToOne(fetch = FetchType.EAGER)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private Discount discount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Season season;

    @ManyToOne
    private Category category;

    private Float price;

    private Float salePrice;
}
