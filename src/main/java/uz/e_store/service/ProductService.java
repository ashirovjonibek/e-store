package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.e_store.entity.Brand;
import uz.e_store.entity.Category;
import uz.e_store.repository.*;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    GenderRepository genderRepository;



}
