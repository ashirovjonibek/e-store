package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Category;
import uz.e_store.entity.Season;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDto extends AbsDtoNameTemplate {

    private String name;

    public static SeasonDto response(Season season, String expand) {
        SeasonDto categoryDto = new SeasonDto();
        categoryDto.setName(season.getName());
        categoryDto.setCreatedAt(season.getCreatedAt());
        categoryDto.setUpdatedAt(season.getUpdatedAt());
        categoryDto.setId(season.getId());
        categoryDto.setActive(season.isActive());
        categoryDto.setDescription(season.getDescription());
        if (expand != null) {
            if (expand.contains("createdBy") && season.getCreatedBy() != null) {
                categoryDto.setCreatedBy(CreatedByUpdatedByDto.response(season.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && season.getUpdatedBy() != null) {
                categoryDto.setUpdatedBy(CreatedByUpdatedByDto.response(season.getUpdatedBy()));
            }
        }
        return categoryDto;
    }

    public static SeasonDto response(Season season) {
        SeasonDto categoryDto = new SeasonDto();
        categoryDto.setName(season.getName());
        categoryDto.setCreatedAt(season.getCreatedAt());
        categoryDto.setUpdatedAt(season.getUpdatedAt());
        categoryDto.setId(season.getId());
        categoryDto.setActive(season.isActive());
        categoryDto.setDescription(season.getDescription());
        return categoryDto;
    }

}
