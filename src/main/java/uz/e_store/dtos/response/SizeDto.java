package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeDto extends AbsDtoNameTemplate {

    private String name;

    private CategoryDto category;

    public static SizeDto response(Size size, String expand) {
        SizeDto sizeDto = new SizeDto();
        sizeDto.setName(size.getName());
        sizeDto.setCreatedAt(size.getCreatedAt());
        sizeDto.setUpdatedAt(size.getUpdatedAt());
        sizeDto.setId(size.getId());
        sizeDto.setActive(size.isActive());
        sizeDto.setDescription(size.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && size.getCreatedBy() != null) {
                sizeDto.setCreatedBy(CreatedByUpdatedByDto.response(size.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && size.getUpdatedBy() != null) {
                sizeDto.setUpdatedBy(CreatedByUpdatedByDto.response(size.getUpdatedBy()));
            }
            if (expand.contains("category") && size.getCategory() != null) {
                sizeDto.setCategory(CategoryDto.response(size.getCategory(),null));
            }
        }
        return sizeDto;
    }

}
