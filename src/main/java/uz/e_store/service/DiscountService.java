package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.response.DiscountDto;
import uz.e_store.dtos.request.DiscountRequest;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Discount;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.DiscountRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    public ApiResponse findAll(int page, int size, String expand, String order) {
        String[] split = order.split("~");
        try {
            Pageable pageable = split.length > 0 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page, size, order);
            Page<Discount> all = discountRepository.findAllByDeleteFalse(pageable);
            List<DiscountDto> collect = all.stream().map(discount -> DiscountDto.response(discount, expand)).collect(Collectors.toList());
            return new ApiResponseList((short) 1, "All discounts!", new Meta(
                    all.getTotalPages(),
                    all.getSize(),
                    page,
                    all.getTotalElements()
            ), collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read discounts!", null);
        }
    }

    public ApiResponse save(Discount discount) {
        try {
            discountRepository.save(discount);
            return new ApiResponse((short) 1, "Successfully saved discount!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved discount try again!", null);
        }
    }

    public ApiResponse edit(UUID id, DiscountRequest discountRequest) {
        Discount discount = DiscountRequest.request(discountRequest);
        discount.setId(id);
        try {
            discountRepository.save(discount);
            return new ApiResponse((short) 1, "Discount successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update discount", null);
        }
    }

    public ApiResponse delete(UUID id) {
        try{
            Optional<Discount> byId = discountRepository.findById(id);
            if (byId.isPresent()){
                Discount discount = byId.get();
                discount.setDelete(true);
                discountRepository.save(discount);
                return new ApiResponse(1,"Discount deleted successfully!",null);
            }else {
                return new ApiResponse(0,"Discount not fount!",null);
            }
        }catch (Exception e){
            return new ApiResponse(0,"Error delete discount!",null);
        }
    }
}
