package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.GenderRequest;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.BrandService;
import uz.e_store.service.GenderService;
import uz.e_store.validators.BrandValidator;
import uz.e_store.validators.GenderValidator;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/gender")
public class GenderController {

    @Autowired
    GenderService genderService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllGender(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(genderService.findAll(page,size,expand,order));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneGender(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "", required = false) String expand
    ) {
        return ResponseEntity.ok(genderService.findById(id,expand));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveGender(@RequestBody GenderRequest genderRequest) {
        Map<String, Object> validate = GenderValidator.validate(genderRequest,genderService.checkName(genderRequest.getName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(genderService.save(GenderRequest.request(genderRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editGender(@PathVariable Integer id, @RequestBody GenderRequest genderRequest) {
        Map<String, Object> validate = GenderValidator.validate(
                genderRequest,
                genderService.checkName(genderRequest.getName())&&
                        !genderService.checkName(genderRequest.getName(),id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(genderService.edit(id, genderRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteGender(@RequestParam Integer id) {
            return ResponseEntity.ok(genderService.delete(id));
    }
}
