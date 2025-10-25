package com.cba.store.mappers;


import com.cba.store.dtos.ProductDto;
import com.cba.store.dtos.RegisterProductRequest;
import com.cba.store.dtos.UpdateUserRequest;
import com.cba.store.entities.Product;
import com.cba.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);
    Product toEntity(RegisterProductRequest request);
    void updateProduct(RegisterProductRequest request, @MappingTarget Product product);
}
