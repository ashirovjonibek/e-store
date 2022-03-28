package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.dtos.response.ProductDto;
import uz.e_store.entity.*;
import uz.e_store.payload.ApiResponse;
import uz.e_store.repository.*;
import uz.e_store.validators.ProductValidator;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    AttachmentService attachmentService;

    public ResponseEntity<?> findAll(String expand) {
        try {
            List<Product> all = productRepository.findAll();
            List<ProductDto> collect = all.stream().map(product -> ProductDto.response(product, expand)).collect(Collectors.toList());
            return ResponseEntity.status(200).body(new ApiResponse(1, "All products",collect));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get all product", null));
        }
    }

    public ResponseEntity<?> findById(UUID id,String expand){
        Optional<Product> product = productRepository.findByIdAndDeleteFalse(id);
        return product.map(value -> ResponseEntity.status(200).body(new ApiResponse(1, "Get one product!", ProductDto.response(value, expand)))).orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with id!", null)));
    }

    public ResponseEntity<?> save(ProductRequest productRequest) {
        try {
            ProductValidator productValidator = validateRef(productRequest);
            if (productValidator.getValidatorErrors().size() > 0) {
                return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", productValidator.getValidatorErrors()));
            } else {
                Product product = productValidator.getProduct();
                product.setAttachments(attachmentService.uploadFile(Arrays.asList(productRequest.getPhotos())));
                productRepository.save(product);
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(1, "Product saved successfully", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error save product", null));
        }
    }

    private ProductValidator validateRef(ProductRequest productRequest) {
        Map<String, Object> validate = ProductValidator.validate(productRequest);
        Product product = ProductRequest.request(productRequest);
        if (product.getBrand() != null) {
            Optional<Brand> brand = brandRepository.findByIdAndDeleteFalse(product.getBrand().getId());
            if (brand.isPresent()) {
                product.setBrand(brand.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Brand not found with id");
                validate.put("brandId", err);
            }
        }
        if (product.getCategory() != null) {
            Optional<Category> category = categoryRepository.findByIdAndDeleteFalse(product.getCategory().getId());
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Category not found with id");
                validate.put("categoryId", err);
            }
        }
        if (product.getSize() != null) {
            Optional<Size> size = sizeRepository.findByIdAndDeleteFalse(product.getSize().getId());
            if (size.isPresent()) {
                product.setSize(size.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Size not found with id");
                validate.put("sizeId", err);
            }
        }
        if (product.getDiscount() != null) {
            Optional<Discount> discount = discountRepository.findByIdAndDeleteFalse(product.getDiscount().getId());
            if (discount.isPresent()) {
                product.setDiscount(discount.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Discount not found with id");
                validate.put("discountId", err);
            }
        }
        if (product.getSeason() != null) {
            Optional<Season> season = seasonRepository.findByIdAndDeleteFalse(product.getSeason().getId());
            if (season.isPresent()) {
                product.setSeason(season.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Season not found with id");
                validate.put("seasonId", err);
            }
        }
        if (product.getGender() != null) {
            Optional<Gender> gender = genderRepository.findByIdAndDeleteFalse(product.getGender().getId());
            if (gender.isPresent()) {
                product.setGender(gender.get());
            } else {
                List<String> err = new ArrayList<>();
                err.add("Gender not found with id");
                validate.put("genderId", err);
            }
        }
        return new ProductValidator(product, validate);
    }

}
