package uz.e_store.validators;

import uz.e_store.dtos.request.BrandFeatureRequest;
import uz.e_store.dtos.request.BrandRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandFeatureValidator {
    public static Map<String, Object> validate(BrandFeatureRequest brandRequest) {
        Map<String, Object> errors = new HashMap<>();
        if (brandRequest.getDescription() == null) {
            List<String> err = new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description", err);
        }
        if (brandRequest.getName() == null) {
            List<String> err = new ArrayList<>();
            err.add("Name not blank!");
            errors.put("name", err);
        }
        if (brandRequest.getBrandId() == null) {
            List<String> err = new ArrayList<>();
            err.add("Brand id not blank!");
            errors.put("brandId", err);
        }
        return errors;
    }

}
