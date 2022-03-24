package uz.e_store.dtos.response;

import lombok.*;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Discount;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto extends AbsDtoTemplate {

    private float percent;

    private Timestamp expirationDate;

    public static DiscountDto response(Discount discount, String expand) {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setExpirationDate(discount.getExpirationDate());
        discountDto.setPercent(discount.getPercent());
        discountDto.setCreatedAt(discount.getCreatedAt());
        discountDto.setUpdatedAt(discount.getUpdatedAt());
        discountDto.setId(discount.getId());
        discountDto.setActive(discount.isActive());
        discountDto.setDescription(discount.getDescription());
        if (expand.contains("createdBy")&&discount.getCreatedBy()!=null) {
            discountDto.setCreatedBy(CreatedByUpdatedByDto.response(discount.getCreatedBy()));
        }
        if (expand.contains("updatedBy")&&discount.getUpdatedBy()!=null) {
            discountDto.setUpdatedBy(CreatedByUpdatedByDto.response(discount.getUpdatedBy()));
        }
        return discountDto;
    }

}
