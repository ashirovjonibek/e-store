package uz.e_store.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class ProductRequest {
    private String name;

    private String description;

    private boolean active;

    private Integer categoryId;

    private Integer sizeId;

    private Integer brandId;

    private Integer genderId;

    private Integer seasonId;

    private UUID discountId;

    private MultipartFile[] photos;
}
