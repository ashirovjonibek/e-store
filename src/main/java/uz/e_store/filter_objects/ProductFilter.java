package uz.e_store.filter_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private Integer categoryId;

    private Integer sizeId;

    private Integer brandId;

    private Integer genderId;

    private Integer seasonId;

    private UUID discountId;

    private String search;

    private String salePriceIn;
}
