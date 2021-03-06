package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Order;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends AbsDtoTemplate {

    private Object user;

    private Object product;

    private int count;

    private int confirm;

    private String color;

    private String size;

    public static OrderDto response(Order order, String expand) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());
        orderDto.setId(order.getId());
        orderDto.setActive(order.isActive());
        orderDto.setDescription(order.getDescription());
        orderDto.setCount(order.getCount());
        orderDto.setConfirm(order.getConfirm());
        orderDto.setColor(order.getColor());
        orderDto.setSize(order.getSize());
        if (expand != null) {
            if (expand.contains("createdBy") && order.getCreatedBy() != null) {
                orderDto.setCreatedBy(CreatedByUpdatedByDto.response(order.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && order.getUpdatedBy() != null) {
                orderDto.setUpdatedBy(CreatedByUpdatedByDto.response(order.getUpdatedBy()));
            }
        }
        if (order.getUser()!=null){
            if (expand!=null&&expand.contains("user")){
                orderDto.setUser(CurrentUserDto.response(order.getUser()));
            }else {
                orderDto.setUser(order.getUser().getId());
            }
        }
        if (order.getProduct()!=null){
            if (expand!=null&&expand.contains("product")){
                orderDto.setProduct(ProductDto.response(order.getProduct(),null));
            }else {
                orderDto.setProduct(order.getProduct().getId());
            }
        }
        return orderDto;
    }

}
