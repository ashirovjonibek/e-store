package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Gender;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderDto extends AbsDtoNameTemplate {

    private String name;

    public static GenderDto response(Gender gender, String expand) {
        GenderDto brandDto = new GenderDto();
        brandDto.setName(gender.getName());
        brandDto.setCreatedAt(gender.getCreatedAt());
        brandDto.setUpdatedAt(gender.getUpdatedAt());
        brandDto.setId(gender.getId());
        brandDto.setActive(gender.isActive());
        brandDto.setDescription(gender.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && gender.getCreatedBy() != null) {
                brandDto.setCreatedBy(CreatedByUpdatedByDto.response(gender.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && gender.getUpdatedBy() != null) {
                brandDto.setUpdatedBy(CreatedByUpdatedByDto.response(gender.getUpdatedBy()));
            }
        }
        return brandDto;
    }

}
