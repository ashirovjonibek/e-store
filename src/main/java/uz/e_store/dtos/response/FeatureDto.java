package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Features;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureDto {
    private UUID id;

    private UUID productId;

    private String name;

    private String description;

    private int active;

    public static FeatureDto response(Features feature){
        FeatureDto dto=new FeatureDto();
        dto.setId(feature.getId());
        dto.setActive(feature.isActive()?1:0);
        dto.setDescription(feature.getDescription());
        dto.setName(feature.getFeatureName());
        dto.setProductId(feature.getProduct().getId());
        return dto;
    }
}
