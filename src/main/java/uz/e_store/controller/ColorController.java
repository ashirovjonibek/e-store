package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.ColorRequest;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.BrandService;
import uz.e_store.service.ColorService;
import uz.e_store.validators.BrandValidator;
import uz.e_store.validators.ColorValidator;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/color")
public class ColorController {

    @Autowired
    ColorService colorService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(colorService.findAll(page,size,expand,order));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "", required = false) String expand
    ) {
        return ResponseEntity.ok(colorService.findById(id,expand));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveBrand(@RequestBody ColorRequest colorRequest) {
        Map<String, Object> validate = ColorValidator.validate(colorRequest,colorService.checkName(colorRequest.getColorHex()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(colorService.save(ColorRequest.request(colorRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody ColorRequest colorRequest) {
        Map<String, Object> validate = ColorValidator.validate(
                colorRequest,
                colorService.checkName(colorRequest.getColorHex())&&
                        !colorService.checkName(colorRequest.getColorHex(),id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(colorService.edit(id, colorRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam Integer id) {
            return ResponseEntity.ok(colorService.delete(id));
    }
}
