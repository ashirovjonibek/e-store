package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Blog;
import uz.e_store.entity.Color;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto extends AbsDtoNameTemplate {

    private String title;

    private Object photo;

    public static BlogDto response(Blog blog, String expand) {
        BlogDto blogDto = new BlogDto();
        blogDto.setCreatedAt(blog.getCreatedAt());
        blogDto.setUpdatedAt(blog.getUpdatedAt());
        blogDto.setId(blog.getId());
        blogDto.setActive(blog.isActive()?1:0);
        blogDto.setDescription(blog.getDescription());
        blogDto.setTitle(blog.getTitle());
        if (expand != null) {
            if (expand.contains("createdBy") && blog.getCreatedBy() != null) {
                blogDto.setCreatedBy(CreatedByUpdatedByDto.response(blog.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && blog.getUpdatedBy() != null) {
                blogDto.setUpdatedBy(CreatedByUpdatedByDto.response(blog.getUpdatedBy()));
            }
            if (expand.contains("createdBy") && blog.getCreatedBy() != null) {
                blogDto.setCreatedBy(CreatedByUpdatedByDto.response(blog.getCreatedBy()));
            }
            if (blog.getPhoto() != null) {
                blogDto.setPhoto(blog.getPhoto().getId());
            }
        }
        return blogDto;
    }

}
