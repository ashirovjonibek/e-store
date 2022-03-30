package uz.e_store.validators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.entity.Product;
import uz.e_store.utils.AppConstants;

import java.lang.reflect.Array;
import java.util.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductValidator {
    private Product product;

    private Map<String, Object> validatorErrors;

    public static Map<String, Object> validate(ProductRequest productRequest,boolean create) {
        Map<String, Object> errors = new HashMap<>();
        if (productRequest.getName() == null) {
            List<String> err = new ArrayList<>();
            err.add("Product name not blank!");
            errors.put("name", err);
        }
        if (productRequest.getDescription() == null) {
            List<String> err = new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description", err);
        }
        if (productRequest.getShortDescription()==null){
            List<String> err = new ArrayList<>();
            err.add("Product short description not blank!");
            errors.put("shortDescription", err);
        }
        if (productRequest.getPrice() == null) {
            List<String> err = new ArrayList<>();
            err.add("Product price not equal 0!");
            errors.put("price", err);
        }
        if (productRequest.getSalePrice() == null) {
            List<String> err = new ArrayList<>();
            err.add("Product sale price not equal 0!");
            errors.put("salePrice", err);
        }
        if (productRequest.getPhotos() == null) {
            if (create){
                List<String> err = new ArrayList<>();
                err.add("Product photo is required!");
                errors.put("photos", err);
            }
        } else {
            Set<String> err = new HashSet<>();
            List<MultipartFile> photos = Arrays.asList(productRequest.getPhotos());
            photos.stream().forEach(photo -> {
                if (!photo.getContentType().toLowerCase().startsWith("image")) {
                    err.add("File type required image");
                } else if (photo.getSize() > AppConstants.FILE_SIZE) {
                    err.add("File size not more then " + AppConstants.FILE_SIZE / 1024 + " Kb");
                }
            });
            if (err.size() > 0) {
                errors.put("photos", err);
            }
        }
        return errors;
    }

}
