package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.BrandFeature;
import uz.e_store.entity.Features;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandFeatureRequest {
    private String description;

    private int active;

    private Integer brandId;

    private String name;

    public static BrandFeature request(BrandFeatureRequest request) {
        BrandFeature features = new BrandFeature();
        features.setDescription(request.getDescription());
        features.setName(request.getName());
        features.setActive(request.getActive() == 1);
        return features;
    }
}
