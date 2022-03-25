package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Category;
import uz.e_store.entity.Gender;
import uz.e_store.entity.Season;
import uz.e_store.repository.GenderRepository;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    private String description;

    private boolean active;

    private String name;

    private Integer seasonId;

    private Integer genderId;

    public static Category request(CategoryRequest categoryRequest) {
        Category category = new Category();
        if (categoryRequest.getGenderId()!=null) {
            Season season = new Season();
            season.setId(categoryRequest.getSeasonId());
            category.setSeason(season);
        }
        if (categoryRequest.getSeasonId()!=null) {
            Gender gender = new Gender();
            gender.setId(categoryRequest.getGenderId());
            category.setGender(gender);
        }
        category.setName(categoryRequest.getName());
        category.setActive(categoryRequest.isActive());
        category.setDescription(categoryRequest.getDescription());
        return category;
    }

}
