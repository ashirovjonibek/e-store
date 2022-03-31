package uz.e_store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends AbsEntity {

    @ManyToOne
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    private int count;

    private int confirm;
}
