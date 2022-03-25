package uz.e_store.validators;

import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.entity.Season;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeasonValidator {
    public static Map<String,Object> validate(SeasonRequest seasonRequest, boolean existsName) {
        Map<String,Object> errors = new HashMap<>();
        if (seasonRequest.getDescription() == null) {
            List<String> err=new ArrayList<>();
            err.add("Description field not blank!");
            errors.put("description",err);
        }
        if (seasonRequest.getName() == null||existsName) {
            List<String> err=new ArrayList<>();
            if (seasonRequest.getName()==null){
                err.add("Season name not blank!");
            }
            if (existsName){
                err.add("This season name arly exists!");
            }
            errors.put("name",err);
        }
        return errors;
    }

}
