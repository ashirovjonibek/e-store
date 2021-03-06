package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.GenderRequest;
import uz.e_store.dtos.response.BrandDto;
import uz.e_store.dtos.response.GenderDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Gender;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.BrandRepository;
import uz.e_store.repository.GenderRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenderService {
    @Autowired
    GenderRepository genderRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order!=null?order.split("~"):new String[0];
        try {
            Pageable pageable = order!=null?split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order):
                    CommonUtils.getPageable(page - 1,size);
            Page<Gender> all = genderRepository.findAllByDeleteFalse(pageable);
            List<GenderDto> collect = all.stream().map(gender -> GenderDto.response(gender, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All genders!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read genders!", null);
        }
    }

    public ApiResponse findById(Integer id, String expand) {
        try{
            Optional<Gender> byId = genderRepository.findByIdAndDeleteFalse(id);
            return byId.map(gender -> new ApiResponse(1, "Gender with id", GenderDto.response(gender,expand))).orElseGet(() -> new ApiResponse(0, "Gender not found with id", null));
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse(0,"Error get Gender with id",null);
        }
    }

    public ApiResponse save(Gender gender) {
        try {
            genderRepository.save(gender);
            return new ApiResponse((short) 1, "Successfully saved gender!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved gender try again!", null);
        }
    }

    public ApiResponse edit(Integer id,  GenderRequest genderRequest) {
        Gender gender = GenderRequest.request(genderRequest);
        gender.setId(id);
        try {
            genderRepository.save(gender);
            return new ApiResponse((short) 1, "gender successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update gender", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Gender> byId = genderRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Gender gender = byId.get();
                gender.setDelete(true);
                genderRepository.save(gender);
                return new ApiResponse(1, "Gender deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Gender not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete gender!", null);
        }
    }

    public boolean checkName(String name){
        return genderRepository.existsByName(name);
    }

    public boolean checkName(String name,Integer id){
        return genderRepository.existsByNameAndId(name,id);
    }
}
