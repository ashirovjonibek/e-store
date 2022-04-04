package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.entity.Article;
import uz.e_store.entity.Blog;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {

    private String description;

    private int active;

    private String title;

    private MultipartFile photo;

    public static Article request(ArticleRequest articleRequest) {
        Article article = new Article();
        article.setActive(articleRequest.getActive()==1);
        article.setDescription(articleRequest.getDescription());
        article.setName(articleRequest.getTitle());
        return article;
    }
}
