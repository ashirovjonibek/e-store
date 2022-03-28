package uz.e_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.e_store.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("api/photo")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @GetMapping("/{id}")
    public HttpEntity<?> getPhoto(@PathVariable String id){
        return attachmentService.getFile(UUID.fromString(id));
    }
}
