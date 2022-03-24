package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends AbsEntity {

    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> attachments;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Size> sizes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Gender> genders;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Discount> discounts;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Season> seasons;

    @ManyToOne
    private Category category;

    private float price;

    private float salePrice;
}
