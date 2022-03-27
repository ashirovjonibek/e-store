package uz.e_store.filter_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilter {
    private Integer seasonId;

    private Integer genderId;

    private String search;

    private Integer parentId;
}
