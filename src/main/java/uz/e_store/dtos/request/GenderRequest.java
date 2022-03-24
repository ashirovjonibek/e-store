package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderRequest {

    private String description;

    private boolean active;

    private String name;

    public static Gender request(GenderRequest brandRequest) {
        Gender gender = new Gender();
        gender.setName(brandRequest.getName());
        gender.setActive(brandRequest.isActive());
        gender.setDescription(brandRequest.getDescription());
        return gender;
    }

}
