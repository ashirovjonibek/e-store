package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResToken {
    private String type="Bearer ";
    private String token;

    private Date expireDate;

    public ResToken(String token) {
        this.token = token;
    }

    public ResToken(String token, Date expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }
}
