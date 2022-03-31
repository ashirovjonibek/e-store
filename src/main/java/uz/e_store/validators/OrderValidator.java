package uz.e_store.validators;

import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.OrderRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderValidator {
    public static Map<String,Object> validate(OrderRequest orderRequest) {
        Map<String,Object> errors = new HashMap<>();
        if (orderRequest.getProductId() == null) {
            List<String> err=new ArrayList<>();
            err.add("Product id not blank!");
            errors.put("productId",err);
        }
        if (orderRequest.getPhoneNumber() == null) {
            List<String> err=new ArrayList<>();
            err.add("Phone number is required!");
            errors.put("phoneNumber",err);
        }
        if (orderRequest.getFullName() == null) {
            List<String> err=new ArrayList<>();
            err.add("Full name is required!");
            errors.put("fullName",err);
        }
        return errors;
    }

}
