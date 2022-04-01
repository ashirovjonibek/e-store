package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;

    private String description;

    private String shortDescription;

    private boolean active;

    private Integer categoryId;

    private Integer sizeId;

    private Integer brandId;

    private Integer genderId;

    private Integer seasonId;

    private Integer discountId;

    private Integer[] colors;

    private MultipartFile[] photos;

    private Float price;

    private Float salePrice;

    public static Product request(ProductRequest productRequest){
        Product product=new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setActive(productRequest.isActive());
        product.setPrice(productRequest.getPrice());
        product.setSalePrice(productRequest.getSalePrice());
        product.setShortDescription(productRequest.getShortDescription());
        if (productRequest.getCategoryId()!=null){
            Category category=new Category();
            category.setId(productRequest.getCategoryId());
            product.setCategory(category);
        }
        if (productRequest.getSizeId()!=null){
            Size size=new Size();
            size.setId(productRequest.getSizeId());
            product.setSize(size);
        }
        if (productRequest.getBrandId()!=null){
            Brand brand=new Brand();
            brand.setId(productRequest.getBrandId());
            product.setBrand(brand);
        }
        if (productRequest.getGenderId()!=null){
            Gender gender=new Gender();
            gender.setId(productRequest.getGenderId());
            product.setGender(gender);
        }
        if (productRequest.getSeasonId()!=null){
            Season season=new Season();
            season.setId(productRequest.getSeasonId());
            product.setSeason(season);
        }
        if (productRequest.getDiscountId()!=null){
            Discount discount=new Discount();
            discount.setId(productRequest.getDiscountId());
            product.setDiscount(discount);
        }
        return product;
    }
}
