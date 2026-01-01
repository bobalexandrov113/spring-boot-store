package com.cba.store.products;



import com.cba.store.entities.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategoryId(Byte categoryId);

    boolean existsByName(@NotBlank String name);
}
