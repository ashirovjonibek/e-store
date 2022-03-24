package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.DiscountRequest;
import uz.e_store.entity.Discount;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.DiscountService;
import uz.e_store.validators.DiscountValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllDiscount(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(discountService.findAll(page,size,expand,order));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveDiscount(@RequestBody DiscountRequest discountRequest) {
        Map<String, Object> validate = DiscountValidator.validate(discountRequest);
        if (validate.size() == 0) {
            return ResponseEntity.ok(discountService.save(DiscountRequest.request(discountRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editDiscount(@PathVariable String id, @RequestBody DiscountRequest discountRequest) {
        Map<String, Object> validate = DiscountValidator.validate(discountRequest);
        if (validate.size() == 0) {
            return ResponseEntity.ok(discountService.edit(UUID.fromString(id), discountRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteDiscount(@RequestParam String id) {
            return ResponseEntity.ok(discountService.delete(UUID.fromString(id)));
    }
}
