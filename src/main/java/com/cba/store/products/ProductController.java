package com.cba.store.products;

import com.cba.store.common.ErrorDto;
import com.cba.store.entities.Image;
import com.cba.store.entities.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/images/{id}")
    public ResponseEntity<List<Image>> getProductImages(@PathVariable Long id)
    {
        List<Image> images= productRepository.findImagesByProductId(id);
        if(images.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(images);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id)
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productMapper.productToProductDto(product));
    }

    @PostMapping
    public ResponseEntity<?> registerProduct(
            @Valid @RequestBody RegisterProductRequest request
    )
    {
        if(productRepository.existsByName(request.getName()))
        {
            return ResponseEntity.badRequest().body(
                   new ErrorDto("product already exists")
            );
        }
        var product = productMapper.toEntity(request);
        product = productRepository.save(product);
        var uri = URI.create("/products/" + product.getId());
        ProductDto productDto = productMapper.productToProductDto(product);
        return ResponseEntity.created(uri).body(productDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductById(@PathVariable Long id, @RequestBody RegisterProductRequest request)
    {
        var product = productRepository.findById(id).orElse(null);
        if(product == null)
            return ResponseEntity.notFound().build();
        productMapper.updateProduct(request, product);
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.productToProductDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id)
    {
        var product = productRepository.findById(id).orElse(null);
        if(product == null)
            return ResponseEntity.notFound().build();
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }



}
