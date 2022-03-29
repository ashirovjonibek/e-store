package uz.e_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.e_store.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByIdAndDeleteFalse(UUID id);

    Page<Product> findAllByDeleteFalse(Pageable pageable);

//    List<Product> findAllByIdAndDelete(Iterable<UUID> uuids, boolean isDelete);

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer genderId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, String search, Pageable pageable
    );

    Page<Product> findAllByCategoryIdAndSizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndDeleteFalse(
            Integer categoryId, Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, Pageable pageable
    );

    //category
    Page<Product> findAllByBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndGenderIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer genderId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, String search, Pageable pageable
    );

    Page<Product> findAllBySizeIdAndBrandIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndDeleteFalse(
            Integer sizeId, Integer brandId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, Pageable pageable
    );

    //size brand
    Page<Product> findAllByCategoryIdAndGenderIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer genderId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    //size gender
    Page<Product> findAllByCategoryIdAndBrandIdAndSeasonIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer brandId, Integer seasonId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );

    //size season
    Page<Product> findAllByCategoryIdAndBrandIdAndGenderIdAndDiscountIdAndSalePriceBetweenAndNameContainingIgnoreCaseAndDeleteFalse(
            Integer categoryId, Integer brandId, Integer genderId, UUID discountId, Float saleFrom, Float saleTo, String search, Pageable pageable
    );
}
