package uz.e_store.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.filter_objects.CategoryFilter;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.BrandService;
import uz.e_store.service.CategoryService;
import uz.e_store.validators.BrandValidator;
import uz.e_store.validators.CategoryValidator;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllCategory(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size,
            @RequestParam(required = false) Integer genderId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) String search
    ) {

        return ResponseEntity.ok(categoryService.findAll(page,size,expand,order,new CategoryFilter(seasonId,genderId,search)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveCategory(@RequestBody CategoryRequest categoryRequest) {
        Map<String, Object> validate = CategoryValidator.validate(categoryRequest,categoryService.checkName(categoryRequest.getName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(categoryService.save(CategoryRequest.request(categoryRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
        Map<String, Object> validate = CategoryValidator.validate(
                categoryRequest,
                categoryService.checkName(categoryRequest.getName())&&
                        !categoryService.checkName(categoryRequest.getName(),id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(categoryService.edit(id, categoryRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteCategory(@RequestParam Integer id) {
            return ResponseEntity.ok(categoryService.delete(id));
    }
}
