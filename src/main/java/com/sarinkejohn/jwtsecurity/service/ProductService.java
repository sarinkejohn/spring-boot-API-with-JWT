package com.sarinkejohn.jwtsecurity.service;

import com.sarinkejohn.jwtsecurity.model.Product;
import com.sarinkejohn.jwtsecurity.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //get all products
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    //get products by id
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    //save/update product
   public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    //Delete product
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
