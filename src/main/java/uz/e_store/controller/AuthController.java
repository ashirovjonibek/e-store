package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.response.CurrentUserDto;
import uz.e_store.dtos.response.ResToken;
import uz.e_store.dtos.request.SignIn;
import uz.e_store.entity.User;
import uz.e_store.secret.CurrentUser;
import uz.e_store.service.AuthService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody SignIn signIn) {
        ResToken resToken = authService.signIn(signIn);
        return ResponseEntity.status(resToken != null ? 200 : 401).body(resToken);
    }

    @GetMapping("/me")
    public HttpEntity<?> me(@CurrentUser User user){
        return ResponseEntity.ok(CurrentUserDto.response(user));
    }
}
