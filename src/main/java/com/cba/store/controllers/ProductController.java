package com.cba.store.controllers;

import com.cba.store.dtos.ProductDto;
import com.cba.store.entities.Product;
import com.cba.store.mappers.ProductMapper;
import com.cba.store.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController

@RequestMapping("/products")
public class ProductController {
    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false, defaultValue = "name", name = "sort") String sort
    )
    {
        if(!Set.of("id","name","categoryId").contains(sort))
            sort = "name";

      List<Product> products = productRepository.findAll(
              Sort.by(sort).ascending( )
      );
      if(products.isEmpty())
          return ResponseEntity.noContent().build();

      return ResponseEntity.ok(

              products.stream()
              .map(productMapper::productToProductDto)
              .toList()
      );

    }

    @GetMapping("/productsCategory")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategoryId(
            @RequestParam(required = false, defaultValue = "1", name = "categoryId") Byte categoryId
    )
    {

        List<Product> products ;

        if(categoryId != null)
        {
            products = productRepository.findByCategoryId(categoryId);
        }
        else
        {
            products = productRepository.findAll();

        }

        if(products.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(

                products.stream()
                        .map(productMapper::productToProductDto)
                        .toList()
        );

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id)
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productMapper.productToProductDto(product));
    }


}
