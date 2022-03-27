package uz.e_store.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.ProductRequest;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/product")
public class ProductController {

    @PostMapping
    public HttpEntity<?> save(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) int active,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer sizeId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer genderId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) String discountId,
            MultipartFile[] photos){
        Map<String,Object> obj=new HashMap<>();
        for (int i = 0; i < photos.length; i++) {
            obj.put(photos[i].getOriginalFilename(),photos[i].getContentType());
        }
        obj.put("productName",name);
        obj.put("description",description);
        obj.put("active",active);
        obj.put("categoryId",categoryId);
        obj.put("sizeId",sizeId);
        obj.put("brandId",brandId);
        obj.put("genderId",genderId);
        obj.put("seasonId",seasonId);
        obj.put("discountId",discountId);
        return ResponseEntity.ok(obj);
    }


}
