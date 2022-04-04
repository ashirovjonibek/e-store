package uz.e_store.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Contact;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest{
    private String phoneNumber;

    private int active;

    private String fullName;

    private String email;

    private String description;

    private int confirm;
}
