package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private int totalPage;
    private int perPage;
    private int currentPage;
    private long totalElement;
}
