package uz.e_store.validators;

import uz.e_store.dtos.request.ContactRequest;
import uz.e_store.dtos.request.OrderRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactValidator {
    public static Map<String,Object> validate(ContactRequest contactRequest) {
        Map<String,Object> errors = new HashMap<>();
        if (contactRequest.getPhoneNumber() == null) {
            List<String> err=new ArrayList<>();
            err.add("Phone number is required!");
            errors.put("phoneNumber",err);
        }
        if (contactRequest.getFullName() == null) {
            List<String> err=new ArrayList<>();
            err.add("Full name is required!");
            errors.put("fullName",err);
        }
        if (contactRequest.getEmail()!=null&&!contactRequest.getEmail().contains("@")){
            List<String> err=new ArrayList<>();
            if(contactRequest.getEmail()==null){
                err.add("Email is required!");
            }
            if (!contactRequest.getEmail().contains("@")){
                err.add("Email must contain an @ sign!");
            }
            errors.put("email",err);
        }
        return errors;
    }

}
