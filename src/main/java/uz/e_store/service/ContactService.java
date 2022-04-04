package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.ContactRequest;
import uz.e_store.dtos.request.OrderRequest;
import uz.e_store.dtos.response.ContactDto;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.OrderDto;
import uz.e_store.entity.Contact;
import uz.e_store.entity.Order;
import uz.e_store.entity.Product;
import uz.e_store.entity.User;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.ContactRepository;
import uz.e_store.repository.OrderRepository;
import uz.e_store.repository.ProductRepository;
import uz.e_store.repository.UserRepository;
import uz.e_store.utils.CommonUtils;
import uz.e_store.validators.ContactValidator;
import uz.e_store.validators.OrderValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> save(ContactRequest contactRequest) {
        Map<String, Object> validate = ContactValidator.validate(contactRequest);
        if (validate.size() == 0) {
            try {
                User user = null;
                Optional<User> user1 = userRepository.findByPhoneNumber(contactRequest.getPhoneNumber());
                user = user1.orElseGet(() -> userRepository.save(new User(contactRequest.getFullName(), contactRequest.getFullName(), contactRequest.getPhoneNumber(), contactRequest.getEmail())));
                Contact contact = new Contact(user, contactRequest.getConfirm());
                contact.setActive(contactRequest.getActive() == 1);
                contact.setDescription(contactRequest.getDescription());
                contact.setName("---");
                contactRepository.save(contact);
                return ResponseEntity.ok(new ApiResponse(1, "Contact saved successfully", null));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error saved contact!", null));
            }
        } else {
            return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
        }
    }

    public ResponseEntity<?> findAll(int page, int size, String expand, String order, UUID userId) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Contact> all = filter(userId, pageable);
            List<ContactDto> collect = all.stream().map(contact -> ContactDto.response(contact, expand)).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponseList((short) 1, "All contacts!", new Meta(
                    all.getTotalPages(),
                    all.getSize(),
                    page,
                    all.getTotalElements()
            ), collect));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error read contacts!", null));
        }
    }

    public HttpEntity<?> deleteOrder(Integer id) {
        Optional<Contact> byId = contactRepository.findById(id);
        if (byId.isPresent()) {
            try {
                contactRepository.delete(byId.get());
                return ResponseEntity.ok(new ApiResponse(1, "Contact deleted successfully!", null));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error for deleted contact", null));
            }
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(0, "Contact not found with id", null));
        }
    }

    private Page<Contact> filter(UUID userId, Pageable pageable) {
        if (userId != null) {
            return contactRepository.findAllByUserId(userId, pageable);
        } else {
            return contactRepository.findAll(pageable);
        }
    }

}
