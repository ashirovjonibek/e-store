package uz.e_store.validators;

import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.dtos.request.GenderRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryValidator {
    public static Map<String,Object> validate(CategoryRequest categoryRequest, boolean existsName) {
        Map<String,Object> errors = new HashMap<>();
        if (categoryRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (categoryRequest.getName() == null||existsName) {
            List<String> err=new ArrayList<>();
            if (categoryRequest.getName()==null){
                err.add("Category name not blank!");
            }
            if (existsName){
                err.add("This category name arly exists!");
            }
            errors.put("name",err);
        }
        return errors;
    }

}
