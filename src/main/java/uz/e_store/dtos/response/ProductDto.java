package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Product;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends AbsDtoTemplate {

    private Object photos;

    private String name;

    private String shortDescription;

    private Object brand;

    private Object size;

    private Object gender;

    private Object discount;

    private Object season;

    private Object category;

    private Float price;

    private Float salePrice;

    public static ProductDto response(Product product, String expand) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setShortDescription(product.getShortDescription());
        productDto.setId(product.getId());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());
        productDto.setActive(product.isActive());
        if (!(authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))) productDto.setPrice(product.getPrice());
        productDto.setSalePrice(product.getSalePrice());
        if (product.getAttachments() != null) {
            productDto.setPhotos(product.getAttachments().stream().map(attachment -> attachment.getId()));
        }
        if (product.getBrand() != null) {
            if (expand != null && expand.contains("brand")) {
                productDto.setBrand(BrandDto.response(product.getBrand(), null));
            } else productDto.setBrand(product.getBrand().getId());
        }
        if (product.getSize() != null) {
            if (expand != null && expand.contains("size")) {
                productDto.setSize(SizeDto.response(product.getSize(), null));
            } else productDto.setSize(product.getSize().getId());
        }
        if (product.getDiscount() != null) {
            if (expand != null && expand.contains("discount")) {
                productDto.setDiscount(DiscountDto.response(product.getDiscount(), null));
            } else productDto.setDiscount(product.getDiscount().getId());
        }
        if (product.getGender() != null) {
            if (expand != null && expand.contains("gender")) {
                productDto.setGender(GenderDto.response(product.getGender(), null));
            } else productDto.setGender(product.getGender().getId());
        }
        if (product.getSeason() != null) {
            if (expand != null && expand.contains("season")) {
                productDto.setSeason(SeasonDto.response(product.getSeason(), null));
            } else productDto.setSeason(product.getSeason().getId());
        }
        if (product.getCategory() != null) {
            if (expand != null && expand.contains("category")) {
                productDto.setCategory(CategoryDto.response(product.getCategory(), null));
            } else productDto.setCategory(product.getCategory().getId());
        }

        if (expand != null) {
            if (expand.contains("createdBy") && product.getCreatedBy() != null) {
                productDto.setCreatedBy(CreatedByUpdatedByDto.response(product.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && product.getUpdatedBy() != null) {
                productDto.setUpdatedBy(CreatedByUpdatedByDto.response(product.getUpdatedBy()));
            }
        }

        return productDto;
    }
}
