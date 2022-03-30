package uz.e_store.filter_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String categoryId;

    private String sizeId;

    private String brandId;

    private String genderId;

    private String seasonId;

    private String discountId;

    private String search;

    private String salePriceIn;
}
