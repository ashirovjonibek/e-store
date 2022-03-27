package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends AbsDtoNameTemplate {

    private String name;

    private Integer seasonId;

    private Integer genderId;

    private GenderDto gender;

    private SeasonDto season;

    private List<CategoryDto> children;

    public static CategoryDto response(Category category, String expand) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setCreatedAt(category.getCreatedAt());
        categoryDto.setUpdatedAt(category.getUpdatedAt());
        categoryDto.setId(category.getId());
        categoryDto.setActive(category.isActive());
        categoryDto.setDescription(category.getDescription());
        if(category.getSeason()!=null){
            categoryDto.setSeasonId(category.getSeason().getId());
        }
        if(category.getGender()!=null){
            categoryDto.setGenderId(category.getGender().getId());
        }
        if (expand != null) {
            if (expand.contains("createdBy") && category.getCreatedBy() != null) {
                categoryDto.setCreatedBy(CreatedByUpdatedByDto.response(category.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && category.getUpdatedBy() != null) {
                categoryDto.setUpdatedBy(CreatedByUpdatedByDto.response(category.getUpdatedBy()));
            }
            if (expand.contains("season") && category.getSeason() != null) {
                categoryDto.setSeason(SeasonDto.response(category.getSeason()));
            }
            if (expand.contains("gender") && category.getGender() != null) {
                categoryDto.setGender(GenderDto.response(category.getGender()));
            }
        }
        return categoryDto;
    }

    public static CategoryDto response(Category category, String expand,List<Category> children) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setCreatedAt(category.getCreatedAt());
        categoryDto.setUpdatedAt(category.getUpdatedAt());
        categoryDto.setId(category.getId());
        categoryDto.setActive(category.isActive());
        categoryDto.setDescription(category.getDescription());
        if(category.getSeason()!=null){
            categoryDto.setSeasonId(category.getSeason().getId());
        }
        if(category.getGender()!=null){
            categoryDto.setGenderId(category.getGender().getId());
        }
        if (expand != null) {
            if (expand.contains("createdBy") && category.getCreatedBy() != null) {
                categoryDto.setCreatedBy(CreatedByUpdatedByDto.response(category.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && category.getUpdatedBy() != null) {
                categoryDto.setUpdatedBy(CreatedByUpdatedByDto.response(category.getUpdatedBy()));
            }
            if (expand.contains("season") && category.getSeason() != null) {
                categoryDto.setSeason(SeasonDto.response(category.getSeason()));
            }
            if (expand.contains("gender") && category.getGender() != null) {
                categoryDto.setGender(GenderDto.response(category.getGender()));
            }
            if (expand.contains("children") && children != null) {
                categoryDto.setChildren(
                        children.stream().map(
                                child->CategoryDto.response(child,expand)
                        ).collect(Collectors.toList())
                );
            }
        }
        return categoryDto;
    }

}
