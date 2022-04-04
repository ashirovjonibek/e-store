package uz.e_store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.ArticleRequest;
import uz.e_store.dtos.response.ArticleDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Article;
import uz.e_store.entity.Attachment;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.ArticleRepository;
import uz.e_store.utils.CommonUtils;
import uz.e_store.validators.ArticleValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final AttachmentService attachmentService;

    public ArticleService(ArticleRepository articleRepository, AttachmentService attachmentService) {
        this.articleRepository = articleRepository;
        this.attachmentService = attachmentService;
    }

    public ResponseEntity<?> findAll(int page, int size, String expand, String order) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Article> all = articleRepository.findAllByDeleteFalse(pageable);
            List<ArticleDto> collect = all.stream().map(article -> ArticleDto.response(article, expand)).collect(Collectors.toList());
            return ResponseEntity.ok(
                    new ApiResponseList(
                            1,
                            "All articles!",
                            new Meta(
                                    all.getTotalPages(),
                                    all.getSize(),
                                    page,
                                    all.getTotalElements()
                            ),
                            collect)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body( new ApiResponse((short) 0, "Error read articles!", null));
        }
    }

    public ResponseEntity<?> findById(Integer id, String expand) {
        try {
            Optional<Article> byId = articleRepository.findByIdAndDeleteFalse(id);
            return byId.map(article -> ResponseEntity.ok(new ApiResponse(1, "Article with id", ArticleDto.response(article, expand)))).orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(0, "Article not found with id", null)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get article with id", null));
        }
    }

    public ResponseEntity<?> save(ArticleRequest articleRequest) {
        Map<String, Object> validate = ArticleValidator.validate(articleRequest, true);
        if (validate.size() == 0) {
            try {
                Article article = ArticleRequest.request(articleRequest);
                List<MultipartFile> files = new ArrayList<>();
                files.add(articleRequest.getPhoto());
                List<Attachment> attachments = attachmentService.uploadFile(files);
                if (attachments.size() > 0) {
                    article.setPhoto(attachments.get(0));
                }
                articleRepository.save(article);
                return ResponseEntity.ok(new ApiResponse((short) 1, "Successfully saved article!", null));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error saved article try again!", null));
            }
        } else return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
    }

    public ResponseEntity<?> edit(Integer id, ArticleRequest articleRequest) {
        Map<String, Object> validate = ArticleValidator.validate(articleRequest, false);
        Optional<Article> article = articleRepository.findByIdAndDeleteFalse(id);
        if (article.isPresent()){
            if (validate.size() == 0) {
                try {
                    Article request = ArticleRequest.request(articleRequest);
                    if (articleRequest.getPhoto() != null) {
                        List<MultipartFile> files = new ArrayList<>();
                        files.add(articleRequest.getPhoto());
                        List<Attachment> attachments = attachmentService.uploadFile(files);
                        if (attachments.size() > 0) {
                            request.setPhoto(attachments.get(0));
                        }else {
                            request.setPhoto(article.get().getPhoto());
                        }
                    }
                    request.setId(id);
                    articleRepository.save(request);
                    return ResponseEntity.ok(new ApiResponse((short) 1, "Successfully updated article!", null));
                } catch (Exception e) {
                    return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error updated article try again!", null));
                }
            } else return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
        }else return ResponseEntity.status(404).body(new ApiResponse(0,"Article not found with id",null));
    }

    public ResponseEntity<?> delete(Integer id) {
        try {
            Optional<Article> byId = articleRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Article article = byId.get();
                article.setDelete(true);
                articleRepository.save(article);
                return ResponseEntity.ok(new ApiResponse(1, "Article deleted successfully!", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Article not fount!", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error delete article!", null));
        }
    }

}
