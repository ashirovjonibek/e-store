package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.DiscountRequest;
import uz.e_store.entity.Brand;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.BrandService;
import uz.e_store.service.DiscountService;
import uz.e_store.validators.BrandValidator;
import uz.e_store.validators.DiscountValidator;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllBrand(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(brandService.findAll(page,size,expand,order));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveBrand(@RequestBody BrandRequest brandRequest) {
        Map<String, Object> validate = BrandValidator.validate(brandRequest,brandService.checkBrandName(brandRequest.getBrandName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(brandService.save(BrandRequest.request(brandRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editDiscount(@PathVariable Integer id, @RequestBody BrandRequest brandRequest) {
        Map<String, Object> validate = BrandValidator.validate(
                brandRequest,
                brandService.checkBrandName(brandRequest.getBrandName())&&
                        !brandService.checkBrandName(brandRequest.getBrandName(),id)
        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(brandService.edit(id, brandRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteDiscount(@RequestParam Integer id) {
            return ResponseEntity.ok(brandService.delete(id));
    }
}
