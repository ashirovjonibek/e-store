package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.ProductRequest;
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
    JdbcTemplate jdbcTemplate;

//    @PersistenceContext
//    private EntityManagerFactory entityManagerFactory;

    public ResponseEntity<?> findAll(String expand, ProductFilter productFilter, int size, int page, String order) {
        List<UUID> uuids = new ArrayList<>();
        try {

            List<Boolean> id = jdbcTemplate.query(getSql(productFilter, page, size, order),
                    (rs, rowNum) ->
                            uuids.add(UUID.fromString(rs.getString("id")))
            );
            Pageable pageable = CommonUtils.getPageable(page, size);
            Page<Product> products = productRepository.findAllByDeleteFalse(pageable);
            List<Product> all = productRepository.findAllById((Iterable<UUID>)uuids);
            List<ProductDto> collect = all.stream().map(product -> ProductDto.response(product, expand)).collect(Collectors.toList());
            return ResponseEntity.status(200).body(new ApiResponseList(1, "All products", new Meta(products.getTotalPages(),size,page,products.getTotalElements()),collect));
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

    private String getSql(ProductFilter filter, int page, int size, String order) {
        StringBuffer stringBuffer = new StringBuffer("select id from product");
        if (filter != null) {
            Integer categoryId = filter.getCategoryId(), sizeId = filter.getSizeId(), brandId = filter.getBrandId(), genderId = filter.getGenderId(), seasonId = filter.getSeasonId();
            UUID discountId = filter.getDiscountId();
            String[] sales = filter.getSalePriceIn() != null ? filter.getSalePriceIn().split("~") : null;
            Float saleFrom = sales != null ? Float.valueOf(sales[0]) : null, saleTo = sales != null ? Float.valueOf(sales[1]) : null;
            String search = filter.getSearch();
            if (categoryId != null) {
                stringBuffer.append(" where category_id=" + categoryId);
            }
            if (sizeId != null) {
                if (categoryId == null) {
                    stringBuffer.append(" where size_id=" + sizeId);
                } else {
                    stringBuffer.append(" and size_id=" + sizeId);
                }
            }
            if (brandId != null) {
                if (categoryId == null && sizeId == null) {
                    stringBuffer.append(" where brand_id=" + brandId);
                } else {
                    stringBuffer.append(" and brand_id=" + brandId);
                }
            }
            if (genderId != null) {
                if (categoryId == null && sizeId == null && brandId == null) {
                    stringBuffer.append(" where gender_id=" + genderId);
                } else {
                    stringBuffer.append(" and gender_id=" + genderId);
                }
            }
            if (seasonId != null) {
                if (categoryId == null && sizeId == null && brandId == null && genderId == null) {
                    stringBuffer.append(" where season_id=" + seasonId);
                } else {
                    stringBuffer.append(" and season_id=" + seasonId);
                }
            }
            if (discountId != null) {
                if (categoryId == null && sizeId == null && brandId == null && genderId == null && seasonId == null) {
                    stringBuffer.append(" where discount_id='" + discountId + "'");
                } else {
                    stringBuffer.append(" and discount_id='" + discountId + "'");
                }
            }
            if (saleFrom != null) {
                if (categoryId == null && sizeId == null && brandId == null && genderId == null && seasonId == null && discountId == null) {
                    stringBuffer.append(" where sale_price between " + saleFrom + " and " + saleTo);
                } else {
                    stringBuffer.append(" and sale_price between " + saleFrom + " and " + saleTo);
                }
            }
            if (search != null) {
                if (categoryId == null && sizeId == null && brandId == null && genderId == null && seasonId == null && discountId == null && saleFrom == null) {
                    stringBuffer.append(" where lower(name) like '%" + search.toLowerCase() + "%'");
                } else {
                    stringBuffer.append(" and lower(name) like '%" + search.toLowerCase() + "%'");
                }
            }
        }
        String pageable = pageable(page, size, order);
        stringBuffer.append(pageable);
        System.out.println(pageable);
        return String.valueOf(stringBuffer);
    }

    private String pageable(int page, int size, String order) {
        String[] split = order != null ? order.split("~") : new String[0];
        StringBuffer stringBuffer = new StringBuffer(split.length > 1 ? " order by " + split[0] + " " + split[1] : "");
        return String.valueOf(stringBuffer.append(" offset " + ((page - 1) * size) + " limit " + (size)));
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
