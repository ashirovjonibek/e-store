package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.e_store.dtos.request.OrderRequest;
import uz.e_store.repository.OrderRepository;
import uz.e_store.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> save(OrderRequest orderRequest){
        return null;
    }
}
