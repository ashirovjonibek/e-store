package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.e_store.dtos.request.OrderRequest;
import uz.e_store.service.OrderService;
import uz.e_store.utils.AppConstants;

import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> findAll(
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_SIZE) int size,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE) int page,
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String productId

    ){
        return orderService.findAll(page,size,expand,order, productId!=null?UUID.fromString(productId):null,userId!=null?UUID.fromString(userId):null);
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody OrderRequest orderRequest){
        return orderService.save(orderRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable String id){
        return orderService.deleteOrder(UUID.fromString(id));
    }
}
