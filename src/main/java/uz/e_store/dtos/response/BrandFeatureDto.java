package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.BrandFeature;
import uz.e_store.entity.Features;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandFeatureDto {
    private Integer id;

    private Integer brandId;

    private String name;

    private String description;

    private int active;

    public static BrandFeatureDto response(BrandFeature feature){
        BrandFeatureDto dto=new BrandFeatureDto();
        dto.setId(feature.getId());
        dto.setActive(feature.isActive()?1:0);
        dto.setDescription(feature.getDescription());
        dto.setName(feature.getName());
        dto.setBrandId(feature.getBrand().getId());
        return dto;
    }
}
