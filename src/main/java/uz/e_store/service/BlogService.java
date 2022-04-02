package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.BlogRequest;
import uz.e_store.dtos.response.BlogDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Attachment;
import uz.e_store.entity.Blog;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.BlogRepository;
import uz.e_store.utils.CommonUtils;
import uz.e_store.validators.BlogValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    AttachmentService attachmentService;

    public ResponseEntity<?> findAll(int page, int size, String expand, String order) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Blog> all = blogRepository.findAllByDeleteFalse(pageable);
            List<BlogDto> collect = all.stream().map(blog -> BlogDto.response(blog, expand)).collect(Collectors.toList());
            return ResponseEntity.ok(
                    new ApiResponseList(
                            1,
                            "All blogs!",
                            new Meta(
                                    all.getTotalPages(),
                                    all.getSize(),
                                    page,
                                    all.getTotalElements()
                            ),
                            collect)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body( new ApiResponse((short) 0, "Error read blogs!", null));
        }
    }

    public ResponseEntity<?> findById(Integer id, String expand) {
        try {
            Optional<Blog> byId = blogRepository.findByIdAndDeleteFalse(id);
            return byId.map(blog -> ResponseEntity.ok(new ApiResponse(1, "Blog with id", BlogDto.response(blog, expand)))).orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(0, "Blog not found with id", null)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get blog with id", null));
        }
    }

    public ResponseEntity<?> save(BlogRequest blogRequest) {
        Map<String, Object> validate = BlogValidator.validate(blogRequest, true);
        if (validate.size() == 0) {
            try {
                Blog request = BlogRequest.request(blogRequest);
                List<MultipartFile> files = new ArrayList<>();
                files.add(blogRequest.getPhoto());
                List<Attachment> attachments = attachmentService.uploadFile(files);
                if (attachments.size() > 0) {
                    request.setPhoto(attachments.get(0));
                }
                blogRepository.save(request);
                return ResponseEntity.ok(new ApiResponse((short) 1, "Successfully saved blog!", null));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error saved blog try again!", null));
            }
        } else return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
    }

    public ResponseEntity<?> edit(Integer id, BlogRequest blogRequest) {
        Map<String, Object> validate = BlogValidator.validate(blogRequest, false);
        if (validate.size() == 0) {
            try {
                Blog request = BlogRequest.request(blogRequest);
                if (blogRequest.getPhoto() != null) {
                    List<MultipartFile> files = new ArrayList<>();
                    files.add(blogRequest.getPhoto());
                    List<Attachment> attachments = attachmentService.uploadFile(files);
                    if (attachments.size() > 0) {
                        request.setPhoto(attachments.get(0));
                    }
                }
                request.setId(id);
                blogRepository.save(request);
                return ResponseEntity.ok(new ApiResponse((short) 1, "Successfully updated blog!", null));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error updated blog try again!", null));
            }
        } else return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Blog> byId = blogRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Blog blog = byId.get();
                blog.setDelete(true);
                blogRepository.save(blog);
                return new ApiResponse(1, "Blog deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Blog not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete blog!", null);
        }
    }

}
