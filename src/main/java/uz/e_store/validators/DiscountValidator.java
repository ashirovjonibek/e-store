package uz.e_store.validators;

import uz.e_store.dtos.request.DiscountRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountValidator {

    public static Map<String,Object> validate(DiscountRequest discountRequest) {
        Map<String,Object> errors = new HashMap<>();
        if (discountRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (discountRequest.getName() == null) {
            List<String> err=new ArrayList<>();
            err.add("Name field not blank!");
            errors.put("name",err);
        }
        if (discountRequest.getExpirationDate() == null) {
            List<String> err=new ArrayList<>();
            err.add("Expiration date not blank!");
            errors.put("expirationDate",err);
        }
        if (discountRequest.getPercent() == 0.0) {
            List<String> err=new ArrayList<>();
            err.add("Percent not equals 0.0!");
            errors.put("percent",err);
        }
        return errors;
    }

}
