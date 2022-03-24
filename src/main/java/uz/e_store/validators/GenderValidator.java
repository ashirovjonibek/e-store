package uz.e_store.validators;

import uz.e_store.dtos.request.GenderRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenderValidator {
    public static Map<String,Object> validate(GenderRequest genderRequest, boolean existsName) {
        Map<String,Object> errors = new HashMap<>();
        if (genderRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (genderRequest.getName() == null||existsName) {
            List<String> err=new ArrayList<>();
            if (genderRequest.getName()==null){
                err.add("Gender name not blank!");
            }
            if (existsName){
                err.add("This gender name arly exists!");
            }
            errors.put("name",err);
        }
        return errors;
    }

}
