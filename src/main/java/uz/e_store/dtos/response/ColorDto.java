package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Color;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto extends AbsDtoNameTemplate {

    private String colorHex;

    public static ColorDto response(Color color, String expand) {
        ColorDto colorDto = new ColorDto();
        colorDto.setCreatedAt(color.getCreatedAt());
        colorDto.setUpdatedAt(color.getUpdatedAt());
        colorDto.setId(color.getId());
        colorDto.setActive(color.isActive());
        colorDto.setColorHex(color.getColorHex());
        colorDto.setDescription(color.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && color.getCreatedBy() != null) {
                colorDto.setCreatedBy(CreatedByUpdatedByDto.response(color.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && color.getUpdatedBy() != null) {
                colorDto.setUpdatedBy(CreatedByUpdatedByDto.response(color.getUpdatedBy()));
            }
        }
        return colorDto;
    }

}
