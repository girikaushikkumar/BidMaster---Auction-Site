package com.auction.services;

import com.auction.payload.ProductDto;
import com.auction.payload.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto,Integer sellerId,Integer subCategoryId);

    ProductDto updateProduct(ProductDto productDto,Integer productId);

    ProductDto getProcutById(Integer productId);

    List<ProductDto> getAllProduct();

    //Pagination
    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy);

    void deleteProduct(Integer productId);

    List<ProductDto> getProductBySeller(Integer sellerId);

    List<ProductDto> getProductByCategory(Integer categoryId);

    List<ProductDto> getProductBySubCategory(Integer subCategoryId);

    List<ProductDto> searchProductByProductName(String productName);
}
