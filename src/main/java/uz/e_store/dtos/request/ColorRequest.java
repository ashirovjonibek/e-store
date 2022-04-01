package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Color;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorRequest {

    private String description;

    private int active;

    private String colorHex;

    private String name;

    public static Color request(ColorRequest colorRequest) {
        Color color = new Color();
        color.setActive(colorRequest.getActive()==1);
        color.setDescription(colorRequest.getDescription());
        color.setColorHex(colorRequest.getColorHex());
        color.setName(colorRequest.getName());
        return color;
    }
}
