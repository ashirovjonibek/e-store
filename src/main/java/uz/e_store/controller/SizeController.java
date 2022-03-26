package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.dtos.request.SizeRequest;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.SeasonService;
import uz.e_store.service.SizeService;
import uz.e_store.validators.SeasonValidator;
import uz.e_store.validators.SizeValidator;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/size")
public class SizeController {

    @Autowired
    SizeService sizeService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllSize(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(sizeService.findAll(page,size,expand,order));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneSize(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "", required = false) String expand
    ) {
        return ResponseEntity.ok(sizeService.findById(id,expand));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveSize(@RequestBody SizeRequest sizeRequest) {
        Map<String, Object> validate = SizeValidator.validate(sizeRequest,sizeService.checkName(sizeRequest.getName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(sizeService.save(SizeRequest.request(sizeRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editSize(@PathVariable Integer id, @RequestBody SizeRequest sizeRequest) {
        Map<String, Object> validate = SizeValidator.validate(
                sizeRequest,
                sizeService.checkName(sizeRequest.getName())&&
                        !sizeService.checkName(sizeRequest.getName(),id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(sizeService.edit(id, sizeRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteSize(@RequestParam Integer id) {
            return ResponseEntity.ok(sizeService.delete(id));
    }
}
