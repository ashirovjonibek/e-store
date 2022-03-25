package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeRequest {

    private String description;

    private boolean active;

    private String name;

    public static Size request(SizeRequest sizeRequest) {
        Size size = new Size();
        size.setName(sizeRequest.getName());
        size.setActive(sizeRequest.isActive());
        size.setDescription(sizeRequest.getDescription());
        return size;
    }

}
