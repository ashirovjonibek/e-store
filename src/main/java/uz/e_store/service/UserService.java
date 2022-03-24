package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.e_store.payload.ApiResponse;
import uz.e_store.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ApiResponse findAll() {
        try{
            return new ApiResponse((short)1,"All users!",userRepository.findAll());
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse((short)0,"Error get users",null);
        }
    }
}
