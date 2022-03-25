package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.dtos.request.SizeRequest;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.SeasonDto;
import uz.e_store.dtos.response.SizeDto;
import uz.e_store.entity.Season;
import uz.e_store.entity.Size;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.SeasonRepository;
import uz.e_store.repository.SizeRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order!=null?order.split("~"):new String[0];
        try {
            Pageable pageable = order!=null?split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page-1, size, order):
                    CommonUtils.getPageable(page-1,size);
            Page<Size> all = sizeRepository.findAllByDeleteFalse(pageable);
            List<SizeDto> collect = all.stream().map(size1 -> SizeDto.response(size1, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All sizes!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read sizes!", null);
        }
    }

    public ApiResponse save(Size size) {
        try {
            sizeRepository.save(size);
            return new ApiResponse((short) 1, "Successfully saved size!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved size try again!", null);
        }
    }

    public ApiResponse edit(Integer id,  SizeRequest sizeRequest) {
        Size size = SizeRequest.request(sizeRequest);
        size.setId(id);
        try {
            sizeRepository.save(size);
            return new ApiResponse((short) 1, "Size successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update size", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Size> byId = sizeRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Size size = byId.get();
                size.setDelete(true);
                sizeRepository.save(size);
                return new ApiResponse(1, "Size deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Size not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete Size!", null);
        }
    }

    public boolean checkName(String name){
        return sizeRepository.existsByName(name);
    }

    public boolean checkName(String name,Integer id){
        return sizeRepository.existsByNameAndId(name,id);
    }
}
