package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.OrderRequest;
import uz.e_store.dtos.response.Meta;
import uz.e_store.dtos.response.OrderDto;
import uz.e_store.entity.Order;
import uz.e_store.entity.Product;
import uz.e_store.entity.User;
import uz.e_store.payload.ApiResponse;
import uz.e_store.payload.ApiResponseList;
import uz.e_store.repository.OrderRepository;
import uz.e_store.repository.ProductRepository;
import uz.e_store.repository.UserRepository;
import uz.e_store.utils.CommonUtils;
import uz.e_store.validators.OrderValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<?> save(OrderRequest orderRequest) {
        Map<String, Object> validate = OrderValidator.validate(orderRequest);
        if (validate.size() == 0) {
            Optional<Product> byId = productRepository.findById(orderRequest.getProductId());
            if (byId.isPresent()) {
                try {
                    User user = null;
                    Optional<User> user1 = userRepository.findByPhoneNumber(orderRequest.getPhoneNumber());
                    user = user1.orElseGet(() -> userRepository.save(new User(orderRequest.getFullName(), orderRequest.getFullName(), orderRequest.getPhoneNumber())));
                    Order order = new Order(user, byId.get(), orderRequest.getCount(), orderRequest.getConfirm());
                    orderRepository.save(order);
                    return ResponseEntity.ok(new ApiResponse(1, "Order saved successfully", null));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body(new ApiResponse(0, "Error saved order!", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(0, "Product not found with id", null));
            }
        } else {
            return ResponseEntity.status(422).body(new ApiResponse(0, "Validator errors", validate));
        }
    }

    public ResponseEntity<?> findAll(int page, int size, String expand, String order, UUID productId, UUID userId) {
        String[] split = order != null ? order.split("~") : new String[0];
        try {
            Pageable pageable = order != null ? split.length > 1 ?
                    CommonUtils.getPageable(page - 1, size, split[0], "DESC".equals(split[1].toUpperCase()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC) :
                    CommonUtils.getPageable(page - 1, size, order) :
                    CommonUtils.getPageable(page - 1, size);
            Page<Order> all = filter(productId, userId, pageable);
            List<OrderDto> collect = all.stream().map(order1 -> OrderDto.response(order1, expand)).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponseList((short) 1, "All orders!", new Meta(
                    all.getTotalPages(),
                    all.getSize(),
                    page,
                    all.getTotalElements()
            ), collect));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse((short) 0, "Error read orders!", null));
        }
    }

    public HttpEntity<?> deleteOrder(UUID id) {
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isPresent()) {
            try {
                orderRepository.delete(byId.get());
                return ResponseEntity.ok(new ApiResponse(1, "Order deleted successfully!", null));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ApiResponse(0, "Error for deleted order", null));
            }
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(0, "Order not found with id", null));
        }
    }

    private Page<Order> filter(UUID productId, UUID userId, Pageable pageable) {
        if (productId != null && userId != null) {
            return orderRepository.findAllByProductIdAndUserId(productId, userId, pageable);
        } else if (productId == null && userId != null) {
            return orderRepository.findAllByUserId(userId, pageable);
        } else if (productId != null && userId == null) {
            return orderRepository.findAllByProductId(productId, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }

}
