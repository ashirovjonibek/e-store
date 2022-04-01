package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.ColorRequest;
import uz.e_store.dtos.response.BrandDto;
import uz.e_store.dtos.response.ColorDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Color;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.BrandRepository;
import uz.e_store.repository.ColorRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService {
    @Autowired
    ColorRepository colorRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order!=null?order.split("~"):new String[0];
        try {
            Pageable pageable = order!=null?split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page-1, size, order):
                    CommonUtils.getPageable(page-1,size);
            Page<Color> all = colorRepository.findAllByDeleteFalse(pageable);
            List<ColorDto> collect = all.stream().map(color -> ColorDto.response(color, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All colors!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read colors!", null);
        }
    }

    public ApiResponse findById(Integer id, String expand) {
        try{
            Optional<Color> byId = colorRepository.findByIdAndDeleteFalse(id);
            return byId.map(color -> new ApiResponse(1, "Color with id", ColorDto.response(color,expand))).orElseGet(() -> new ApiResponse(0, "Color not found with id", null));
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse(0,"Error get brand with id",null);
        }
    }

    public ApiResponse save(Color color) {
        try {
            colorRepository.save(color);
            return new ApiResponse((short) 1, "Successfully saved color!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved color try again!", null);
        }
    }

    public ApiResponse edit(Integer id,  ColorRequest colorRequest) {
        Color color = ColorRequest.request(colorRequest);
        color.setId(id);
        try {
            colorRepository.save(color);
            return new ApiResponse((short) 1, "Color successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update color", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Color> byId = colorRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Color color = byId.get();
                color.setDelete(true);
                colorRepository.save(color);
                return new ApiResponse(1, "Color deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Color not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete color!", null);
        }
    }

    public boolean checkName(String hex){
        return colorRepository.existsByColorHexAndDeleteFalse(hex);
    }

    public boolean checkName(String hex,Integer id){
        return colorRepository.existsByColorHexAndIdAndDeleteFalse(hex,id);
    }


}
