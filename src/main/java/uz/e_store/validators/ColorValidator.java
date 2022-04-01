package uz.e_store.validators;

import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.ColorRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorValidator {
    public static Map<String,Object> validate(ColorRequest colorRequest, boolean existsColorHex) {
        Map<String,Object> errors = new HashMap<>();
        if (colorRequest.getColorHex() == null||existsColorHex) {
            List<String> err=new ArrayList<>();
            if (colorRequest.getColorHex()==null){
                err.add("Color hex not blank!");
            }
            if (existsColorHex){
                err.add("This color hex arly exists!");
            }
            errors.put("colorHex",err);
        }
        return errors;
    }

}
