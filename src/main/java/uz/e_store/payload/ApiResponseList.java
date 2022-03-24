package uz.e_store.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.response.DiscountDto;
import uz.e_store.dtos.response.Meta;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseList extends ApiResponse{
    private Meta meta;

    public ApiResponseList(int status, String message, Meta meta, Object data) {
        super(status,message,data);
        this.meta=meta;
    }
}
