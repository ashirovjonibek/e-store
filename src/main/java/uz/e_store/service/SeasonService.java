package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.dtos.response.CategoryDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.SeasonDto;
import uz.e_store.entity.Category;
import uz.e_store.entity.Season;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.CategoryRepository;
import uz.e_store.repository.SeasonRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeasonService {
    @Autowired
    SeasonRepository seasonRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order!=null?order.split("~"):new String[0];
        try {
            Pageable pageable = order!=null?split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page-1, size, order):
                    CommonUtils.getPageable(page-1,size);
            Page<Season> all = seasonRepository.findAllByDeleteFalse(pageable);
            List<SeasonDto> collect = all.stream().map(season -> SeasonDto.response(season, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All seasons!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read seasons!", null);
        }
    }

    public ApiResponse save(Season season) {
        try {
            seasonRepository.save(season);
            return new ApiResponse((short) 1, "Successfully saved season!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved season try again!", null);
        }
    }

    public ApiResponse edit(Integer id,  SeasonRequest seasonRequest) {
        Season season = SeasonRequest.request(seasonRequest);
        season.setId(id);
        try {
            seasonRepository.save(season);
            return new ApiResponse((short) 1, "Season successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update season", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Season> byId = seasonRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Season season = byId.get();
                season.setDelete(true);
                seasonRepository.save(season);
                return new ApiResponse(1, "Season deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Season not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete season!", null);
        }
    }

    public boolean checkName(String name){
        return seasonRepository.existsByName(name);
    }

    public boolean checkName(String name,Integer id){
        return seasonRepository.existsByNameAndId(name,id);
    }
}
