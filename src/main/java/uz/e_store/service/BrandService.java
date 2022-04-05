package uz.e_store.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.BrandFeatureRequest;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.DiscountRequest;
import uz.e_store.dtos.response.BrandDto;
import uz.e_store.dtos.response.BrandFeatureDto;
import uz.e_store.dtos.response.DiscountDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Brand;
import uz.e_store.entity.BrandFeature;
import uz.e_store.entity.Discount;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.BrandFeatureRepository;
import uz.e_store.repository.BrandRepository;
import uz.e_store.repository.DiscountRepository;
import uz.e_store.utils.CommonUtils;
import uz.e_store.validators.BrandFeatureValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandFeatureRepository brandFeatureRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Brand> all = brandRepository.findAllByDeleteFalse(pageable);
            List<BrandDto> collect = all.stream().map(brand -> BrandDto.response(brand, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All brands!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read brands!", null);
        }
    }

    public ApiResponse findById(Integer id, String expand) {
        try {
            Optional<Brand> byId = brandRepository.findByIdAndDeleteFalse(id);
            return byId.map(brand -> new ApiResponse(1, "Brand with id", BrandDto.response(brand, expand))).orElseGet(() -> new ApiResponse(0, "Brand not found with id", null));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(0, "Error get brand with id", null);
        }
    }

    public ApiResponse save(Brand brand) {
        try {
            brandRepository.save(brand);
            return new ApiResponse((short) 1, "Successfully saved brand!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved brand try again!", null);
        }
    }

    public ApiResponse edit(Integer id, BrandRequest brandRequest) {
        Brand brand = BrandRequest.request(brandRequest);
        brand.setId(id);
        try {
            brandRepository.save(brand);
            return new ApiResponse((short) 1, "Brand successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update brand", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Brand> byId = brandRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Brand brand = byId.get();
                brand.setDelete(true);
                brandRepository.save(brand);
                return new ApiResponse(1, "Brand deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Brand not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete brand!", null);
        }
    }

    public ResponseEntity<?> getAllBrandFeatures(Integer brandId) {
        try {
            List<BrandFeature> brandFeatures = brandFeatureRepository.findAllByBrandId(brandId);
            List<BrandFeatureDto> collect = brandFeatures.stream().map(BrandFeatureDto::response).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse(1, "All features with brand id!", collect));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get all features with brand id", null));
        }
    }

    public ResponseEntity<?> getOneBrandFeature(Integer id) {
        try {
            Optional<BrandFeature> brandFeature = brandFeatureRepository.findById(id);
            return brandFeature
                    .map(feature -> ResponseEntity.ok(new ApiResponse(1, "Brand feature with id!", BrandFeatureDto.response(feature))))
                    .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse(1, "Brand feature not found with id!", brandFeature.get())));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(0, "Error get all features with id", null));
        }
    }

    public ResponseEntity<?> saveBrandFeature(BrandFeatureRequest request) {
        Map<String, Object> validate = BrandFeatureValidator.validate(request);
        if (validate.size() == 0) {
            Optional<Brand> brand = brandRepository.findByIdAndDeleteFalse(request.getBrandId());
            if (brand.isPresent()) {
                try {
                    BrandFeature brandFeature = BrandFeatureRequest.request(request);
                    brandFeature.setBrand(brand.get());
                    brandFeatureRepository.save(brandFeature);
                    return ResponseEntity.ok(new ApiResponse(1, "Successfully saved feature!", null));
                } catch (Exception e) {
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error saved brand feature", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Brand not found with id!", null));
            }
        } else {
            return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
        }
    }

    public ResponseEntity<?> editBrandFeature(Integer id, BrandFeatureRequest request) {
        Map<String, Object> validate = BrandFeatureValidator.validate(request);
        if (validate.size() == 0) {
            Optional<BrandFeature> brandFeature = brandFeatureRepository.findById(id);
            if (brandFeature.isPresent()) {
                try {
                    BrandFeature brandFeature1=BrandFeatureRequest.request(request);
                    if (request.getBrandId()!=null){
                        Optional<Brand> brand = brandRepository.findByIdAndDeleteFalse(request.getBrandId());
                        brand.ifPresent(brandFeature1::setBrand);
                    }else brandFeature1.setBrand(brandFeature.get().getBrand());
                    brandFeature1.setId(id);
                    brandFeatureRepository.save(brandFeature1);
                    return ResponseEntity.ok(new ApiResponse(1, "Successfully updated feature!", null));
                } catch (Exception e) {
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error updated brand feature", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Brand feature not found with id!", null));
            }
        } else {
            return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
        }
    }

    public ResponseEntity<?> deleteBrandFeature(Integer id){
        try{
            Optional<BrandFeature> byId = brandFeatureRepository.findById(id);
            if (byId.isPresent()){
                brandFeatureRepository.delete(byId.get());
                return ResponseEntity.ok(new ApiResponse(1,"Successfully deleted brand feature",null));
            }else {
                return ResponseEntity.status(404).body(new ApiResponse(0,"Feature not found with id",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse(0,"Error delete brand feature",null));
        }
    }

    public boolean checkName(String name) {
        return brandRepository.existsByBrandName(name);
    }

    public boolean checkName(String name, Integer id) {
        return brandRepository.existsByBrandNameAndId(name, id);
    }


}
