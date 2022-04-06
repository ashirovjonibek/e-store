package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Discount;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {

    private String description;

    private boolean active;

    private String name;

    private String brandName;

    private MultipartFile photo;

    public BrandRequest(String description, boolean active, String name, String brandName) {
        this.description = description;
        this.active = active;
        this.name = name;
        this.brandName = brandName;
    }

    public static Brand request(BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setBrandName(brandRequest.getBrandName());
        brand.setActive(brandRequest.isActive());
        brand.setDescription(brandRequest.getDescription());
        return brand;
    }
}
