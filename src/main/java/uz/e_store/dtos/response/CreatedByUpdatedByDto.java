package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedByUpdatedByDto {
    private String fullName;

    private String username;

    public static CreatedByUpdatedByDto response(User user) {
        CreatedByUpdatedByDto createdByUpdatedByDto = new CreatedByUpdatedByDto();
        if (user != null) {
            if (user.getLastName() != null && user.getFirstName() != null) {
                createdByUpdatedByDto.setFullName(user.getFirstName() + " " + user.getLastName());
            }
            createdByUpdatedByDto.setUsername(user.getUsername());
            return createdByUpdatedByDto;
        }else return null;
    }
}
