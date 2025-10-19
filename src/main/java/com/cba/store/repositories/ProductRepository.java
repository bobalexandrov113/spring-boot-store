package com.cba.store.repositories;



import com.cba.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategoryId(Byte categoryId);

}
