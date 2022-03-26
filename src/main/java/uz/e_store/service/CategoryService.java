package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.BrandRequest;
import uz.e_store.dtos.request.CategoryRequest;
import uz.e_store.dtos.response.BrandDto;
import uz.e_store.dtos.response.CategoryDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Category;
import uz.e_store.entity.Gender;
import uz.e_store.entity.Season;
import uz.e_store.filter_objects.CategoryFilter;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.BrandRepository;
import uz.e_store.repository.CategoryRepository;
import uz.e_store.repository.GenderRepository;
import uz.e_store.repository.SeasonRepository;
import uz.e_store.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    SeasonRepository seasonRepository;

    public ApiResponse findAll(int page, int size, String expand, String order, CategoryFilter filter) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Category> all = filter == null ? categoryRepository.findAllByDeleteFalse(pageable) : filter(filter, pageable);
            List<CategoryDto> collect = all.stream().map(category -> CategoryDto.response(category, expand)).collect(Collectors.toList());
            return new ApiResponseList(
                    1,
                    "All categories!",
                    new Meta(
                            all.getTotalPages(),
                            all.getSize(),
                            page,
                            all.getTotalElements()
                    ),
                    collect);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error read categories!", null);
        }
    }

    public ApiResponse findById(Integer id, String expand) {
        try {
            Optional<Category> byId = categoryRepository.findByIdAndDeleteFalse(id);
            return byId.map(category -> new ApiResponse(1, "Category with id", CategoryDto.response(category, expand))).orElseGet(() -> new ApiResponse(0, "Category not found with id", null));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(0, "Error get category with id", null);
        }
    }

    public ApiResponse save(Category category) {
        category = findGender(category);
        category = findSeason(category);
        try {
            categoryRepository.save(category);
            return new ApiResponse((short) 1, "Successfully saved category!", null);
        } catch (Exception e) {
            return new ApiResponse((short) 0, "Error saved category try again!", null);
        }
    }

    public ApiResponse edit(Integer id, CategoryRequest categoryRequest) {
        Category category = CategoryRequest.request(categoryRequest);
        category.setId(id);
        category = findGender(category);
        category = findSeason(category);
        try {
            categoryRepository.save(category);
            return new ApiResponse((short) 1, "Category successfully updated!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse((short) 0, "Error update category", null);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            Optional<Category> byId = categoryRepository.findByIdAndDeleteFalse(id);
            if (byId.isPresent()) {
                Category category = byId.get();
                category.setDelete(true);
                categoryRepository.save(category);
                return new ApiResponse(1, "Category deleted successfully!", null);
            } else {
                return new ApiResponse(0, "Category not fount!", null);
            }
        } catch (Exception e) {
            return new ApiResponse(0, "Error delete category!", null);
        }
    }

    public boolean checkName(String name) {
        return categoryRepository.existsByName(name);
    }

    public boolean checkName(String name, Integer id) {
        return categoryRepository.existsByNameAndId(name, id);
    }

    private Category findGender(Category category) {
        if (category.getGender() != null) {
            Optional<Gender> gender = genderRepository.findByIdAndDeleteFalse(category.getGender().getId());
            gender.ifPresent(category::setGender);
        }
        return category;
    }

    private Category findSeason(Category category) {
        if (category.getSeason() != null) {
            Optional<Season> season = seasonRepository.findByIdAndDeleteFalse(category.getSeason().getId());
            season.ifPresent(category::setSeason);
        }
        return category;
    }

    private Page<Category> filter(CategoryFilter filter, Pageable pageable) {
        Page<Category> filter1 = null;
        if (filter.getGenderId() != null && filter.getSeasonId() != null && filter.getSearch() != null) {
            filter1 = categoryRepository.findAllByGenderIdAndSeasonIdAndNameContainingIgnoreCaseAndDeleteFalse(filter.getGenderId(), filter.getSeasonId(), filter.getSearch(), pageable);
        } else if (filter.getGenderId() != null && filter.getSeasonId() != null && filter.getSearch() == null) {
            filter1 = categoryRepository.findAllByGenderIdAndSeasonIdAndDeleteFalse(filter.getGenderId(), filter.getSeasonId(), pageable);
        } else if (filter.getGenderId() != null && filter.getSeasonId() == null && filter.getSearch() != null) {
            filter1 = categoryRepository.findAllByGenderIdAndNameContainingIgnoreCaseAndDeleteFalse(filter.getGenderId(), filter.getSearch(), pageable);
        } else if (filter.getGenderId() == null && filter.getSeasonId() != null && filter.getSearch() != null) {
            filter1 = categoryRepository.findAllBySeasonIdAndNameContainingIgnoreCaseAndDeleteFalse(filter.getSeasonId(), filter.getSearch(), pageable);
        } else if (filter.getGenderId() != null && filter.getSeasonId() == null && filter.getSearch() == null) {
            filter1 = categoryRepository.findAllByGenderIdAndDeleteFalse(filter.getGenderId(), pageable);
        } else if (filter.getGenderId() == null && filter.getSeasonId() != null && filter.getSearch() == null) {
            filter1 = categoryRepository.findAllBySeasonIdAndDeleteFalse(filter.getSeasonId(), pageable);
        } else if (filter.getGenderId() == null && filter.getSeasonId() == null && filter.getSearch() != null) {
            filter1 = categoryRepository.findAllByNameContainingIgnoreCaseAndDeleteFalse(filter.getSearch(), pageable);
        }
        return filter1;
    }
}
