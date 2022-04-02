package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.entity.Blog;
import uz.e_store.entity.Color;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    private String description;

    private int active;

    private String title;

    private MultipartFile photo;

    public static Blog request(BlogRequest blogRequest) {
        Blog blog = new Blog();
        blog.setActive(blogRequest.getActive()==1);
        blog.setDescription(blogRequest.getDescription());
        blog.setTitle(blogRequest.getTitle());
        return blog;
    }
}
