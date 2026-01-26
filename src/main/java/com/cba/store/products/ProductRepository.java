package com.cba.store.products;



import com.cba.store.entities.Image;
import com.cba.store.entities.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategoryId(Byte categoryId);

    boolean existsByName(@NotBlank String name);

    @Query(value = "select i.* from images i join products_images pi on pi.image_id=i.id and pi.product_id=:productId",nativeQuery = true)
    public List<Image> findImagesByProductId(Long productId);
}
