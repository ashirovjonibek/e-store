package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.entity.Article;
import uz.e_store.entity.Blog;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto extends AbsDtoNameTemplate {

    private String title;

    private Object photo;

    private int active;

    public static ArticleDto response(Article article, String expand) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setCreatedAt(article.getCreatedAt());
        articleDto.setUpdatedAt(article.getUpdatedAt());
        articleDto.setId(article.getId());
        articleDto.setActive(article.isActive() ? 1 : 0);
        articleDto.setDescription(article.getDescription());
        articleDto.setTitle(article.getName());
        if (expand != null) {
            if (expand.contains("createdBy") && article.getCreatedBy() != null) {
                articleDto.setCreatedBy(CreatedByUpdatedByDto.response(article.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && article.getUpdatedBy() != null) {
                articleDto.setUpdatedBy(CreatedByUpdatedByDto.response(article.getUpdatedBy()));
            }
        }
        if (article.getPhoto() != null) {
            articleDto.setPhoto(article.getPhoto().getId());
        }
        return articleDto;
    }

}
