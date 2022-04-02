package uz.e_store.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.dtos.request.FeatureRequest;
import uz.e_store.dtos.request.ProductRequest;
import uz.e_store.filter_objects.ProductFilter;
import uz.e_store.service.ProductService;
import uz.e_store.utils.AppConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public HttpEntity<?> getAll(
            @RequestParam(required = false) String expand,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String colorIds,
            @RequestParam(required = false) String order,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_SIZE) int size
    ) {
        ProductFilter productFilter = null;
        if (filter != null) {
            filter = "{" + filter + "}";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            productFilter = gson.fromJson(filter, ProductFilter.class);
        }
        if (filter == null && colorIds != null && !colorIds.equals("")) {
            productFilter = new ProductFilter();
            productFilter.setColorId(colorIds);
        } else if (filter != null && colorIds != null && !colorIds.equals("")) {
            productFilter.setColorId(colorIds);
        }
//        return ResponseEntity.ok(productFilter);
        return productService.findAll(expand, productFilter, size, page, order);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable String id, @RequestParam(required = false) String expand) {
        return productService.findById(UUID.fromString(id), expand);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> save(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) List<Integer> sizeId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer genderId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Float salePrice,
            @RequestParam(required = false) Integer discountId,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false, name = "photos") MultipartFile[] photos
    ) {
        String[] split = colors != null && !colors.equals("") ? colors.split(",") : new String[0];
        Integer[] colorIds = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            colorIds[i] = Integer.valueOf(split[i]);
        }
        ProductRequest productRequest = new ProductRequest(
                name,
                description,
                shortDescription,
                active == 1,
                categoryId,
                sizeId,
                brandId,
                genderId,
                seasonId,
                discountId,
                colorIds,
                photos,
                price,
                salePrice
        );
        return productService.save(productRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) List<Integer> sizeId,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer genderId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Float salePrice,
            @RequestParam(required = false) Integer discountId,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false, name = "photos") MultipartFile[] photos,
            @RequestParam(required = false) String oldPhotos
    ) {
        String[] split = colors != null && !colors.equals("") ? colors.split(",") : new String[0];
        Integer[] colorIds = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            colorIds[i] = Integer.valueOf(split[i]);
        }
        ProductRequest productRequest = new ProductRequest(
                name,
                description,
                shortDescription,
                active == 1,
                categoryId,
                sizeId,
                brandId,
                genderId,
                seasonId,
                discountId,
                colorIds,
                photos,
                price,
                salePrice
        );
        return productService.edit(UUID.fromString(id), productRequest, oldPhotos);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable String id) {
        return productService.delete(UUID.fromString(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/features")
    public HttpEntity<?> saveFeatures(@RequestBody FeatureRequest feature) {
        return productService.saveFeature(feature);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/features/{id}")
    public HttpEntity<?> editFeature(@PathVariable String id, @RequestBody FeatureRequest feature) {
        feature.setId(UUID.fromString(id));
        return productService.editFeature(feature);
    }

    @GetMapping("/get-features/{id}")
    public HttpEntity<?> getFeatures(@PathVariable String id) {
        return productService.getFeatures(UUID.fromString(id));
    }

    @GetMapping("/features/{id}")
    public HttpEntity<?> getOneFeature(@PathVariable String id) {
        return productService.getOneFeature(UUID.fromString(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/features/{id}")
    public HttpEntity<?> deleteFeature(@PathVariable String id) {
        return productService.deleteFeature(UUID.fromString(id));
    }
}
