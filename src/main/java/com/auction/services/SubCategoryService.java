package com.auction.services;

import com.auction.payload.CategoryDto;
import com.auction.payload.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {
    SubCategoryDto createSubCategory(SubCategoryDto subCategory,Integer CatId);

    SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto,Integer subCatId);

    List<SubCategoryDto> getAllSubCategory();

    void deleteSubCategory(Integer subCatId);
}
