package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Discount;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {

    @NotNull(message = "The description field should not be empty")
    private String description;

    private int active;

    @NotNull(message = "The percent field should not be 0.0")
    private float percent;

    @NotNull(message = "The expirationDate field should not be empty")
    private Timestamp expirationDate;

    private String name;

    public static Discount request(DiscountRequest discountRequest) {
        Discount discount = new Discount();
        discount.setExpirationDate(new Date(new java.util.Date(discountRequest.getExpirationDate().getTime()).getTime()));
        discount.setPercent(discountRequest.getPercent());
        discount.setActive(discountRequest.getActive()==1);
        discount.setDescription(discountRequest.getDescription());
        discount.setName(discountRequest.getName());
        return discount;
    }

}
