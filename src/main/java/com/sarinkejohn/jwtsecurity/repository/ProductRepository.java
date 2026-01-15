package com.sarinkejohn.jwtsecurity.repository;

import com.sarinkejohn.jwtsecurity.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
