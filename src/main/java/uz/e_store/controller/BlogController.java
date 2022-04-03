package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.BlogRequest;
import uz.e_store.service.BlogService;

@RestController
@CrossOrigin
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping
    public HttpEntity<?> findAll(
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) String order,
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size
    ){
        return blogService.findAll(page, size, expand, order);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id,String expand){
        return blogService.findById(id,expand);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> save(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) int active,
            @RequestParam(required = false)MultipartFile photo
    ){
        BlogRequest blogRequest=new BlogRequest(description,active,title,photo);
        return blogService.save(blogRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(
            @PathVariable Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) int active,
            @RequestParam(required = false)MultipartFile photo
    ){
        BlogRequest blogRequest=new BlogRequest(description,active,title,photo);
        return blogService.edit(id,blogRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id){
        return blogService.delete(id);
    }
}
