package com.auction.services;

import com.auction.payload.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto,Integer catId);

    List<CategoryDto> getAllCategory();

    void deleteCategory(Integer catId);
}
