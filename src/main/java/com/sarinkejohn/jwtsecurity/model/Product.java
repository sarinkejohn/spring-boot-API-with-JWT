package com.sarinkejohn.jwtsecurity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_seq")
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private BigDecimal price;
    @Size(min = 2, max = 250)
    @Column(nullable = false)
    private String productDescription;

    public Product(String productName, BigDecimal price, String productDescription) {
        this.productName = productName;
        this.price = price;
        this.productDescription = productDescription;
    }
}
