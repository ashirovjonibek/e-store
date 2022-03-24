package uz.e_store.validators;

import org.springframework.beans.factory.annotation.Autowired;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.DiscountRequest;
import uz.e_store.repository.BrandRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandValidator {
    public static Map<String,Object> validate(BrandRequest brandRequest,boolean existsBrandName) {
        Map<String,Object> errors = new HashMap<>();
        if (brandRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (brandRequest.getName() == null) {
            List<String> err=new ArrayList<>();
            err.add("Name not blank!");
            errors.put("name",err);
        }
        if (brandRequest.getBrandName() == null||existsBrandName) {
            List<String> err=new ArrayList<>();
            if (brandRequest.getBrandName()==null){
                err.add("Brand name not blank!");
            }
            if (existsBrandName){
                err.add("This brandName arly exists!");
            }
            errors.put("brandName",err);
        }
        return errors;
    }

}
