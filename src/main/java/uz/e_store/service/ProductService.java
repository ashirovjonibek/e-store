package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.FeatureRequest;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.dtos.response.FeatureDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.ProductDto;
import uz.e_store.entity.*;
import uz.e_store.filter_objects.ProductFilter;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.*;
import uz.e_store.utils.CommonUtils;
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

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> findAll(String expand, ProductFilter productFilter, int size, int page, String order) {
        List<UUID> uuids = new ArrayList<>();
        List<Long> count = new ArrayList<>();
        try {
            System.out.println(getSql(productFilter, "count", page, size, order));
            jdbcTemplate.query(getSql(productFilter, "count", page, size, order),
                    (rs, rowNum) ->
                            count.add(rs.getLong("count"))
            );

            if (count.get(0) != null && count.get(0) > 0 && count.get(0) > (page - 1) * size) {
                jdbcTemplate.query(getSql(productFilter, "prod", page, size, order),
                        (rs, rowNum) ->
                                uuids.add(UUID.fromString(rs.getString("id")))
                );
            }
            List<Product> all = uuids.size() > 0 ? productRepository.findAllById(uuids) : null;
            List<ProductDto> collect = all != null ? all.stream().map(product -> ProductDto.response(product, expand)).collect(Collectors.toList()) : new ArrayList<>();
            return ResponseEntity.status(200).body(new ApiResponseList(1, "All products", getMeta(size, page, count.get(0)), collect));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get all product", null));
        }
    }

    public ResponseEntity<?> findById(UUID id, String expand) {
        Optional<Product> product = productRepository.findByIdAndDeleteFalse(id);
        return product.map(value -> ResponseEntity.status(200).body(new ApiResponse(1, "Get one product!", ProductDto.response(value, expand)))).orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with id!", null)));
    }

    public ResponseEntity<?> save(ProductRequest productRequest) {
        try {
            ProductValidator productValidator = validateRef(productRequest, true);
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

    public ResponseEntity<?> edit(UUID id, ProductRequest productRequest, String oldPhotos) {
        Optional<Product> product1 = productRepository.findByIdAndDeleteFalse(id);
        if (product1.isPresent()) {
            try {
                ProductValidator productValidator = validateRef(productRequest, false);
                if (productValidator.getValidatorErrors().size() > 0) {
                    return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", productValidator.getValidatorErrors()));
                } else {
                    Product product = productValidator.getProduct();
                    product.setId(product1.get().getId());
                    if (productRequest.getPhotos() != null) {
                        List<Attachment> attachments = attachmentService.uploadFile(Arrays.asList(productRequest.getPhotos()));
                        product.setAttachments(attachments);
                    } else {
                        if (product.getAttachments() == null) {
                            product.setAttachments(product1.get().getAttachments());
                        }
                    }
                    List<Attachment> attachments = product.getAttachments();
                    if (oldPhotos != null && !oldPhotos.equals("")) {
                        product1.get().getAttachments().forEach(attachment -> {
                            if (oldPhotos.contains(attachment.getId().toString())) {
                                attachments.add(attachment);
                            }
                        });
                    }
                    product.setAttachments(attachments);
                    productRepository.save(product);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(1, "Product updated successfully", null));
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error update product", null));
            }
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with id", null));
        }
    }

    public ResponseEntity<?> delete(UUID id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            try {
                productRepository.delete(byId.get());
                return ResponseEntity.ok(new ApiResponse(1, "Product deleted successfully", null));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error delete product", null));
            }
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with id", null));
        }
    }

    private ProductValidator validateRef(ProductRequest productRequest, boolean create) {
        Map<String, Object> validate = ProductValidator.validate(productRequest, create);
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
        if (productRequest.getSizeId() != null&&productRequest.getSizeId().size()>0) {
            List<Size> size = sizeRepository.findAllById(productRequest.getSizeId());
            if (size.size()==productRequest.getSizeId().size()) {
                product.setSizes(size);
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
        if (productRequest.getColors() != null && productRequest.getColors().length > 0) {
            List<Color> colors = colorRepository.findAllById(Arrays.asList(productRequest.getColors()));
            if (colors.size() == Arrays.asList(productRequest.getColors()).size()) {
                product.setColors(colors);
            } else {
                List<String> err = new ArrayList<>();
                err.add("Color not found with id");
                validate.put("colors", err);
            }
        }
        return new ProductValidator(product, validate);
    }

    private String getSql(ProductFilter filter, String type, int page, int size, String order) {
        StringBuffer stringBuffer = new StringBuffer(type.equals("prod") ? "select id from product" : "select count(*) from product");
        if (filter != null) {
            String categoryId = filter.getCategoryId(), colorIds = filter.getColorId(), sizeId = filter.getSizeId(), brandId = filter.getBrandId(), genderId = filter.getGenderId(), seasonId = filter.getSeasonId();
            String discountId = filter.getDiscountId();
            Float saleFrom = filter.getSaleFrom() != null && !filter.getSaleFrom().equals("") ? Float.valueOf(filter.getSaleFrom()) : null, saleTo = filter.getSaleTo() != null && !filter.getSaleTo().equals("") ? Float.valueOf(filter.getSaleTo()) : null;
            String search = filter.getSearch();
            if (categoryId != null && !categoryId.equals("")) {
                stringBuffer.append(" where category_id in (" + categoryId + ")");
            }
            if (sizeId != null && !sizeId.equals("")) {
                if (categoryId == null || categoryId.equals("")) {
                    stringBuffer.append(" where id in (select product_id from product_sizes where sizes_id in (" + sizeId + "))");
                } else {
                    stringBuffer.append(" and id in (select product_id from product_sizes where sizes_id in (" + sizeId + "))");
                }
            }
            if (brandId != null && !brandId.equals("")) {
                if ((categoryId == null || categoryId.equals("")) && (sizeId == null || sizeId.equals(""))) {
                    stringBuffer.append(" where brand_id in (" + brandId + ")");
                } else {
                    stringBuffer.append(" and brand_id in (" + brandId + ")");
                }
            }
            if (genderId != null && !genderId.equals("")) {
                if ((categoryId == null || categoryId.equals("")) && (sizeId == null || sizeId.equals("")) && (brandId == null || brandId.equals(""))) {
                    stringBuffer.append(" where gender_id in (" + genderId + ")");
                } else {
                    stringBuffer.append(" and gender_id in (" + genderId + ")");
                }
            }
            if (seasonId != null && !seasonId.equals("")) {
                if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                ) {
                    stringBuffer.append(" where season_id in (" + seasonId + ")");
                } else {
                    stringBuffer.append(" and season_id in (" + seasonId + ")");
                }
            }
            if (discountId != null && !discountId.equals("")) {
                if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                ) {
                    stringBuffer.append(" where discount_id in (" + discountId + ")");
                } else {
                    stringBuffer.append(" and discount_id in (" + discountId + ")");
                }
            }
            if (saleFrom != null || saleTo != null) {
                if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                                && (discountId == null || discountId.equals("")) && saleTo == null && saleFrom != null) {
                    stringBuffer.append(" where sale_price >=" + saleFrom);
                } else if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                                && (discountId == null || discountId.equals("")) && saleFrom == null && saleTo != null) {
                    stringBuffer.append(" where sale_price <= " + saleTo);
                } else if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                                && (discountId == null || discountId.equals("")) && saleFrom != null && saleTo != null) {
                    stringBuffer.append(" where sale_price >= " + saleFrom + " and sale_price <= " + saleTo);
                } else {
                    if (saleFrom == null && saleTo != null) {
                        stringBuffer.append(" and sale_price <= " + saleTo);
                    } else if (saleFrom != null && saleTo == null) {
                        stringBuffer.append(" and sale_price >= " + saleFrom);
                    } else {
                        stringBuffer.append(" and sale_price >= " + saleFrom + " and sale_price <= " + saleTo);
                    }
                }
            }
            if (search != null && !search.equals("")) {
                if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                                && (discountId == null || discountId.equals("")) && saleFrom == null) {
                    stringBuffer.append(" where lower(name) like '%" + search.toLowerCase() + "%'");
                } else {
                    stringBuffer.append(" and lower(name) like '%" + search.toLowerCase() + "%'");
                }
            }
            if (colorIds != null && !colorIds.equals("")) {
                if (
                        (categoryId == null || categoryId.equals(""))
                                && (sizeId == null || sizeId.equals(""))
                                && (brandId == null || brandId.equals(""))
                                && (genderId == null || genderId.equals(""))
                                && (seasonId == null || seasonId.equals(""))
                                && (discountId == null || discountId.equals(""))
                                && saleFrom == null
                                && (search == null || search.equals(""))) {
                    stringBuffer.append(" where id in (select product_id from product_colors where colors_id in (" + colorIds + "))");
                } else {
                    stringBuffer.append(" and id in (select product_id from product_colors where colors_id in (" + colorIds + "))");
                }
            }
        }
        String pageable = type.equals("prod") ? pageable(page, size, order, type) : "";
        stringBuffer.append(pageable);
        System.out.println(pageable);
        return String.valueOf(stringBuffer);
    }

    private String pageable(int page, int size, String order, String type) {
        String[] split = order != null ? order.split("~") : new String[0];
        if (type.equals("prod")) {
            order = split.length > 1 ? " order by " + split[0] + " " + split[1] : "";
        } else {
            order = "";
        }
        StringBuffer stringBuffer = new StringBuffer(order);
        return String.valueOf(stringBuffer.append(" offset " + ((page - 1) * size) + " limit " + (size)));
    }

    private Meta getMeta(int pageSize, int currentPage, Long totalElement) {
        Meta meta = new Meta();
        meta.setCurrentPage(currentPage);
        meta.setPerPage(pageSize);
        meta.setTotalElement(totalElement);
        if (totalElement % pageSize > 0) {
            int a = (int) (totalElement / pageSize + 1);
            meta.setTotalPage(a);
        } else {
            int a = (int) (totalElement / pageSize);
            meta.setTotalPage(a);
        }

        return meta;
    }

    public ResponseEntity<?> saveFeature(FeatureRequest feature) {
        if (feature != null && feature.getProductId() != null) {
            Optional<Product> product = productRepository.findById(feature.getProductId());
            if (product.isPresent()) {
                try {
                    Features request = FeatureRequest.request(feature);
                    request.setProduct(product.get());
                    featureRepository.save(request);
                    return ResponseEntity.status(201).body(new ApiResponse(1, "Feature saved successfully", null));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error save product feature", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with productId", null));
            }
        } else {
            return ResponseEntity.status(400).body(new ApiResponse(0, "Data not allowed", null));
        }
    }

    public ResponseEntity<?> editFeature(FeatureRequest feature) {
        if (feature != null && feature.getProductId() != null) {
            Optional<Product> product = productRepository.findById(feature.getProductId());
            if (product.isPresent()) {
                try {
                    Features request = FeatureRequest.request(feature);
                    request.setProduct(product.get());
                    featureRepository.save(request);
                    return ResponseEntity.status(200).body(new ApiResponse(1, "Feature updated successfully", null));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error update product feature", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with productId", null));
            }
        } else {
            return ResponseEntity.status(400).body(new ApiResponse(0, "Data not allowed", null));
        }
    }

    public ResponseEntity<?> getFeatures(UUID id) {
        try {
            List<Features> features = featureRepository.findAllByProductId(id);
            return ResponseEntity.ok(new ApiResponse(1, "Product features", features.stream().map(FeatureDto::response)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get product features with productId", null));
        }
    }

    public ResponseEntity<?> getOneFeature(UUID id) {
        try {
            Optional<Features> feature = featureRepository.findById(id);
            return feature
                    .map(features -> ResponseEntity.ok(new ApiResponse(1, "Product feature", FeatureDto.response(features))))
                    .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(1, "Product feature not found", null)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get product features with productId", null));
        }
    }

    public ResponseEntity<?> deleteFeature(UUID id) {
        Optional<Features> feature = featureRepository.findById(id);
        if (feature.isPresent()) {
            try {
                featureRepository.delete(feature.get());
                return ResponseEntity.ok(new ApiResponse(1, "Feature deleted successfully", null));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error delete feature", null));
            }
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(0, "Feature not found with id", null));
        }
    }

//  private Page<Product> filter(ProductFilter filter, Pageable pageable) {
//        Integer categoryId = filter.getCategoryId(), sizeId = filter.getSizeId(), brandId = filter.getBrandId(), genderId = filter.getGenderId(), seasonId = filter.getSeasonId();
//        UUID discountId = filter.getDiscountId();
//        String[] sales = filter.getSalePriceIn() != null ? filter.getSalePriceIn().split("~") : null;
//        Float saleFrom = sales != null ? Float.valueOf(sales[0]) : null, saleTo = sales != null ? Float.valueOf(sales[1]) : null;
//        String search = filter.getSearch();
//        if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, brandId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, brandId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId == null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, brandId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId == null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId == null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, brandId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId == null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, brandId, genderId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId == null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, brandId, genderId, seasonId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom == null &&
//                        search != null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndNameContainingIgnoreCaseAndDeleteFalse(
//                    categoryId, sizeId, brandId, genderId, seasonId, discountId, search, pageable
//            );
//        } else if (
//                categoryId != null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search == null
//        ) {
//            return productRepository.findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndDeleteFalse(
//                    categoryId, sizeId, brandId, genderId, seasonId, discountId, saleFrom, saleTo, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId == null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllByBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    brandId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId == null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, genderId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId == null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, brandId, seasonId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId == null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndGenderIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, brandId, genderId, discountId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId == null &&
//                        saleFrom != null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, brandId, genderId, seasonId, saleFrom, saleTo, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom == null &&
//                        search != null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndNameContainingIgnoreCaseAndDeleteFalse(
//                    sizeId, brandId, genderId, seasonId, discountId, search, pageable
//            );
//        } else if (
//                categoryId == null &&
//                        sizeId != null &&
//                        brandId != null &&
//                        genderId != null &&
//                        seasonId != null &&
//                        discountId != null &&
//                        saleFrom != null &&
//                        search == null
//        ) {
//            return productRepository.findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndDeleteFalse(
//                    sizeId, brandId, genderId, seasonId, discountId, saleFrom, saleTo, pageable
//            );
//        }
//    }

    //    private List<Product> filter(ProductFilter filter,Pageable pageable){
//        return (List<Product>) entityManager.createNativeQuery("select * from product where discount_id=null ");
//
//    }

}
