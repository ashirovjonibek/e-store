package uz.e_store.validators;

import uz.e_store.dtos.request.SizeRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SizeValidator {
    public static Map<String,Object> validate(SizeRequest sizeRequest, boolean existsName) {
        Map<String,Object> errors = new HashMap<>();
        if (sizeRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (sizeRequest.getName() == null||existsName) {
            List<String> err=new ArrayList<>();
            if (sizeRequest.getName()==null){
                err.add("Size name not blank!");
            }
            if (existsName){
                err.add("This size name arly exists!");
            }
            errors.put("name",err);
        }
        return errors;
    }

}
