package uz.e_store.validators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.BlogRequest;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.entity.Product;
import uz.e_store.utils.AppConstants;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogValidator {
    private Map<String, Object> validatorErrors;

    public static Map<String, Object> validate(BlogRequest blogRequest, boolean create) {
        Map<String, Object> errors = new HashMap<>();
        if (blogRequest.getDescription() == null) {
            List<String> err = new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description", err);
        }
        if (blogRequest.getTitle() == null) {
            List<String> err = new ArrayList<>();
            err.add("Blog title not blank!");
            errors.put("price", err);
        }
        if (create) {
            if (blogRequest.getPhoto() == null) {
                List<String> err = new ArrayList<>();
                err.add("Blog photo is required!");
                errors.put("photo", err);
            } else {
                Set<String> err = new HashSet<>();
                MultipartFile photo = blogRequest.getPhoto();
                if (!photo.getContentType().toLowerCase().startsWith("image")) {
                    err.add("File type required image");
                } else if (photo.getSize() > AppConstants.FILE_SIZE) {
                    err.add("File size not more then " + AppConstants.FILE_SIZE / 1024 + " Kb");
                }
                if (err.size() > 0) {
                    errors.put("photo", err);
                }
            }
        }
        return errors;
    }
}
