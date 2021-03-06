package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.BrandFeatureRequest;
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
@CrossOrigin("*")
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllBrand(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(brandService.findAll(page, size, expand, order));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneBrand(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "", required = false) String expand
    ) {
        return ResponseEntity.ok(brandService.findById(id, expand));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveBrand(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) boolean active,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) MultipartFile photo
    ) {
        BrandRequest brandRequest=new BrandRequest(description,active,name,brandName,photo);
        Map<String, Object> validate = BrandValidator.validate(brandRequest, brandService.checkName(brandRequest.getBrandName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(brandService.save(brandRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editBrand(
            @PathVariable Integer id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) boolean active,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) MultipartFile photo
    ) {
        BrandRequest brandRequest=new BrandRequest(description,active,name,brandName,photo);
        Map<String, Object> validate = BrandValidator.validate(
                brandRequest,
                brandService.checkName(brandRequest.getBrandName()) &&
                        !brandService.checkName(brandRequest.getBrandName(), id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(brandService.edit(id, brandRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteBrand(@RequestParam Integer id) {
        return ResponseEntity.ok(brandService.delete(id));
    }

    @GetMapping("/feature/{brandId}")
    public HttpEntity<?> getAllFeatureWithBrandId(@PathVariable Integer brandId) {
        return brandService.getAllBrandFeatures(brandId);
    }

    @GetMapping("/feature-one/{id}")
    public HttpEntity<?> getOneFeatureWithId(@PathVariable Integer id) {
        return brandService.getOneBrandFeature(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("feature")
    public HttpEntity<?> saveFeature(@RequestBody BrandFeatureRequest featureRequest) {
        return brandService.saveBrandFeature(featureRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("feature/{id}")
    public HttpEntity<?> editFeature(@PathVariable Integer id, @RequestBody BrandFeatureRequest featureRequest) {
        return brandService.editBrandFeature(id, featureRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("feature/{id}")
    public HttpEntity<?> deleteFeature(@PathVariable Integer id) {
        return brandService.deleteBrandFeature(id);
    }
}
