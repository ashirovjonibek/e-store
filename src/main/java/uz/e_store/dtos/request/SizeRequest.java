package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Category;
import uz.e_store.entity.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeRequest {

    private String description;

    private boolean active;

    private String name;

    private Integer categoryId;

    public static Size request(SizeRequest sizeRequest) {
        Size size = new Size();
        size.setName(sizeRequest.getName());
        size.setActive(sizeRequest.isActive());
        size.setDescription(sizeRequest.getDescription());
        if(sizeRequest.getCategoryId()!=null){
            Category category=new Category();
            category.setId(sizeRequest.getCategoryId());
            size.setCategory(category);
        }
        return size;
    }

}
