package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.payload.ErrorResponse;
import uz.e_store.service.CategoryService;
import uz.e_store.service.SeasonService;
import uz.e_store.validators.CategoryValidator;
import uz.e_store.validators.SeasonValidator;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/season")
public class SeasonController {

    @Autowired
    SeasonService seasonService;

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllSeason(
            @RequestParam(defaultValue = "", required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(seasonService.findAll(page,size,expand,order));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> saveSeason(@RequestBody SeasonRequest seasonRequest) {
        Map<String, Object> validate = SeasonValidator.validate(seasonRequest,seasonService.checkName(seasonRequest.getName()));
        if (validate.size() == 0) {
            return ResponseEntity.ok(seasonService.save(SeasonRequest.request(seasonRequest)));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> editSeason(@PathVariable Integer id, @RequestBody SeasonRequest seasonRequest) {
        Map<String, Object> validate = SeasonValidator.validate(
                seasonRequest,
                seasonService.checkName(seasonRequest.getName())&&
                        !seasonService.checkName(seasonRequest.getName(),id)

        );
        if (validate.size() == 0) {
            return ResponseEntity.ok(seasonService.edit(id, seasonRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(0, "Validator errors!", validate));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping
    public HttpEntity<?> deleteSeason(@RequestParam Integer id) {
            return ResponseEntity.ok(seasonService.delete(id));
    }
}
