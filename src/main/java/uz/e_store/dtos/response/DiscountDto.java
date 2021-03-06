package uz.e_store.dtos.response;

import lombok.*;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Discount;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto extends AbsDtoNameTemplate {

    private float percent;

    private String name;

    private Timestamp expirationDate;

    public static DiscountDto response(Discount discount, String expand) {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setExpirationDate(new Timestamp(new java.util.Date(discount.getExpirationDate().getTime()).getTime()));
        discountDto.setPercent(discount.getPercent());
        discountDto.setCreatedAt(discount.getCreatedAt());
        discountDto.setUpdatedAt(discount.getUpdatedAt());
        discountDto.setId(discount.getId());
        discountDto.setActive(discount.isActive());
        discountDto.setName(discount.getName());
        discountDto.setDescription(discount.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && discount.getCreatedBy() != null) {
                discountDto.setCreatedBy(CreatedByUpdatedByDto.response(discount.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && discount.getUpdatedBy() != null) {
                discountDto.setUpdatedBy(CreatedByUpdatedByDto.response(discount.getUpdatedBy()));
            }
        }
        return discountDto;
    }

}
