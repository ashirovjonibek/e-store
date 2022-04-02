package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @ManyToMany
    private List<Size> sizes;

    @ManyToOne(fetch = FetchType.EAGER)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private Discount discount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Season season;

    @ManyToMany
    private List<Color> colors;

    @ManyToOne
    private Category category;

    private Float price;

    private Float salePrice;

    private boolean delete;

    public Product(String id, String name, BigDecimal price) {
        System.out.println(id+name+price);
    }
}
