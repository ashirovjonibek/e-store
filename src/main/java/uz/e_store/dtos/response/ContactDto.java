package uz.e_store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.e_store.dtos.template.AbsDtoNameTemplate;
import uz.e_store.dtos.template.AbsDtoTemplate;
import uz.e_store.entity.Contact;
import uz.e_store.entity.Order;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto extends AbsDtoNameTemplate {

    private Object user;

    private int confirm;

    private int active;

    public static ContactDto response(Contact contact, String expand) {
        ContactDto contactDto = new ContactDto();
        contactDto.setCreatedAt(contact.getCreatedAt());
        contactDto.setUpdatedAt(contact.getUpdatedAt());
        contactDto.setId(contact.getId());
        contactDto.setActive(contact.isActive() ? 1 : 0);
        contactDto.setConfirm(contact.getConfirm());
        if (expand != null) {
            if (expand.contains("createdBy") && contact.getCreatedBy() != null) {
                contactDto.setCreatedBy(CreatedByUpdatedByDto.response(contact.getCreatedBy()));
            }
            if (expand.contains("updatedBy") && contact.getUpdatedBy() != null) {
                contactDto.setUpdatedBy(CreatedByUpdatedByDto.response(contact.getUpdatedBy()));
            }
        }
        if (contact.getUser() != null) {
            if (expand != null && expand.contains("user")) {
                contactDto.setUser(CurrentUserDto.response(contact.getUser()));
            } else {
                contactDto.setUser(contact.getUser().getId());
            }
        }
        return contactDto;
    }

}
