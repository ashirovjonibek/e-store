package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Order;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest extends AbsDtoTemplate {
    private String phoneNumber;

    private UUID productId;

    private int count;

    private int confirm;

    public  Order request(OrderRequest orderRequest){
        return null;
    }
}
