package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Features;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureRequest {
    private UUID id;

    private String description;

    private int active;

    private UUID productId;

    private String name;

    public static Features request(FeatureRequest request) {
        Features features = new Features();
        if (request.getId()!=null){
            features.setId(request.getId());
        }
        features.setDescription(request.getDescription());
        features.setFeatureName(request.getName());
        features.setActive(request.getActive() == 1);
        return features;
    }
}
