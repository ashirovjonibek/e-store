package uz.e_store.exeptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnprocessableEntity {
    private String message;
    private Object errors;
}
