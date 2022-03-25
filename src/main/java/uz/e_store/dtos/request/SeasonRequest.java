package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Category;
import uz.e_store.entity.Season;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonRequest {

    private String description;

    private boolean active;

    private String name;

    public static Season request(SeasonRequest seasonRequest) {
        Season season = new Season();
        season.setName(seasonRequest.getName());
        season.setActive(seasonRequest.isActive());
        season.setDescription(seasonRequest.getDescription());
        return season;
    }

}
