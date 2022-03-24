package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.e_store.entity.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDto {
    private UUID id;

    private String lastName;

    private String firstName;

    private String username;

    private String phoneNumber;

    private Set<String> roles;

    private String imageUrl;

    public static CurrentUserDto response(User user){
        CurrentUserDto currentUserDto=new CurrentUserDto();
        currentUserDto.setId(user.getId());
        currentUserDto.setLastName(user.getLastName());
        currentUserDto.setFirstName(user.getFirstName());
        currentUserDto.setPhoneNumber(user.getPhoneNumber());
        currentUserDto.setRoles(user.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toSet()));
        currentUserDto.setUsername(user.getUsername());
        if (user.getAttachment()!=null){
            currentUserDto.setImageUrl(user.getAttachment().getUrl());
        }
        return currentUserDto;
    }

}
