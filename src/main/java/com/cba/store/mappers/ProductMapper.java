package com.cba.store.mappers;


import com.cba.store.dtos.ProductDto;
import com.cba.store.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);
}
