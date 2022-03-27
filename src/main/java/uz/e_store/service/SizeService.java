package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.SeasonRequest;
import uz.e_store.dtos.request.SizeRequest;
import uz.e_store.dtos.response.BrandDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.SeasonDto;
import uz.e_store.dtos.response.SizeDto;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Category;
import uz.e_store.entity.Season;
import uz.e_store.entity.Size;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.CategoryRepository;
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

    @Autowired
    CategoryRepository categoryRepository;

    public ApiResponse findAll(int page, int size, String expand, String order, Integer categoryId, String search) {
        try {

            Page<Size> all = filterBy(search, categoryId, pageable(page, size, order));
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

    public ApiResponse findById(Integer id, String expand) {
        try {
            Optional<Size> byId = sizeRepository.findByIdAndDeleteFalse(id);
            return byId
                    .map(size -> new ApiResponse(1, "Size with id", SizeDto.response(size, expand)))
                    .orElseGet(() -> new ApiResponse(0, "Size not found with id", null));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(0, "Error get size with id", null);
        }
    }

    public ApiResponse save(Size size) {
        Size ref = findRef(size);
        try {
            sizeRepository.save(ref);
            return new ApiResponse((short) 1, "Successfully saved size!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved size try again!", null);
        }
    }

    public ApiResponse edit(Integer id, SizeRequest sizeRequest) {
        Size size = SizeRequest.request(sizeRequest);
        size.setId(id);
        Size ref = findRef(size);
        try {
            sizeRepository.save(ref);
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

    public boolean checkName(String name) {
        return sizeRepository.existsByName(name);
    }

    public boolean checkName(String name, Integer id) {
        return sizeRepository.existsByNameAndId(name, id);
    }

    private Size findRef(Size size) {
        if (size.getCategory() != null) {
            Optional<Category> category = categoryRepository.findByIdAndDeleteFalse(size.getCategory().getId());
            category.ifPresent(size::setCategory);
        }
        return size;
    }

    private Page<Size> filterBy(String search, Integer categoryId, Pageable pageable) {
        if (search != null && categoryId != null) {
            return sizeRepository.findAllByCategoryIdAndNameContainingIgnoreCaseAndDeleteFalse(categoryId, search, pageable);
        } else if (search == null && categoryId != null) {
            return sizeRepository.findAllByCategoryIdAndDeleteFalse(categoryId, pageable);
        } else if (search != null && categoryId == null) {
            return sizeRepository.findAllByNameContainingIgnoreCaseAndDeleteFalse(search,pageable);
        }else {
            return sizeRepository.findAllByDeleteFalse(pageable);
        }
    }

    private Pageable pageable(int page, int size, String order) {
        String[] split = order != null ? order.split("~") : new String[0];
        return order != null ? split.length > 1 ?
                CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                        Sort.Direction.DESC : Sort.Direction.ASC) :
                CommonUtils.getPageable(page - 1, size, order) :
                CommonUtils.getPageable(page - 1, size);
    }
}
