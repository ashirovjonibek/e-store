package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.ContactRequest;
import uz.e_store.dtos.request.OrderRequest;
import uz.e_store.service.ContactService;
import uz.e_store.service.OrderService;
import uz.e_store.utils.AppConstants;

import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    ContactService contactService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> findAll(
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_SIZE) int size,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE) int page,
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String userId

    ) {
        return contactService.findAll(page, size, expand, order, userId != null ? UUID.fromString(userId) : null);
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody ContactRequest contactRequest) {
        return contactService.save(contactRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        return contactService.deleteOrder(id);
    }
}
