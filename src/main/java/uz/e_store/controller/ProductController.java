package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.service.ProductService;

import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public HttpEntity<?> getAll(@RequestParam(required = false) String expand) {
        return productService.findAll(expand);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable String id, @RequestParam(required = false) String expand) {
        return productService.findById(UUID.fromString(id),expand);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> save(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer sizeId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer genderId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Float salePrice,
            @RequestParam(required = false) String discountId,
            @RequestParam(required = false, name = "photos") MultipartFile[] photos
    ) {
        ProductRequest productRequest = new ProductRequest(
                name,
                description,
                shortDescription,
                active == 1,
                categoryId,
                sizeId,
                brandId,
                genderId,
                seasonId,
                discountId != null ? UUID.fromString(discountId) : null,
                photos,
                price,
                salePrice
        );
        return productService.save(productRequest);
    }


}
