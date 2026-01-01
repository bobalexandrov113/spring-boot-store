package com.cba.store.products;


import com.cba.store.carts.CartProductDto;
import com.cba.store.entities.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);

    Product toEntity(RegisterProductRequest request);

    void updateProduct(RegisterProductRequest request, @MappingTarget Product product);

    Product toEntity(CartProductDto cartProductDto);

    CartProductDto toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(CartProductDto cartProductDto, @MappingTarget Product product);
}
