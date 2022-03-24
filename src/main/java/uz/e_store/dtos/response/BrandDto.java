package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Brand;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto extends AbsDtoNameTemplate {

    private String name;

    private String brnadName;

    public static BrandDto response(Brand brand, String expand) {
        BrandDto brandDto = new BrandDto();
        brandDto.setBrnadName(brand.getBrandName());
        brandDto.setName(brand.getName());
        brandDto.setCreatedAt(brand.getCreatedAt());
        brandDto.setUpdatedAt(brand.getUpdatedAt());
        brandDto.setId(brand.getId());
        brandDto.setActive(brand.isActive());
        brandDto.setDescription(brand.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && brand.getCreatedBy() != null) {
                brandDto.setCreatedBy(CreatedByUpdatedByDto.response(brand.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && brand.getUpdatedBy() != null) {
                brandDto.setUpdatedBy(CreatedByUpdatedByDto.response(brand.getUpdatedBy()));
            }
        }
        return brandDto;
    }

}
