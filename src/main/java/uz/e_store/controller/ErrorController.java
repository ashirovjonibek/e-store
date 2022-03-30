package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.e_store.repository.ErrorRepository;

@RestController
@RequestMapping("/api/error")
public class ErrorController {
    @Autowired
    ErrorRepository errorRepository;

    @GetMapping
    public HttpEntity<?> getAll(){
        return ResponseEntity.ok(errorRepository.findAll());
    }
}
