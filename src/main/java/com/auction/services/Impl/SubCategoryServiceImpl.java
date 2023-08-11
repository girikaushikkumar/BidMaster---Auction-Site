package com.auction.services.Impl;

import com.auction.Entities.Category;
import com.auction.Entities.SubCategory;
import com.auction.exception.ResourceNotFoundException;
import com.auction.payload.SubCategoryDto;
import com.auction.repositories.CategoryRepo;
import com.auction.repositories.SubCategoryRepo;
import com.auction.services.SubCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto,Integer catId) {
        Category category = this.categoryRepo.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category","Id",catId));
        SubCategory subCategory = this.dtoToSubcategory(subCategoryDto);
        subCategory.setCategory(category);
        SubCategory savelist = this.subCategoryRepo.save(subCategory);
        return this.subCategoryToDto(savelist);
    }

    @Override
    public SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto,Integer subCatId) {

        SubCategory subCategory = this.subCategoryRepo.findById(subCatId).orElseThrow(() -> new ResourceNotFoundException("SubCategory","Id",subCatId));
        subCategory.setSubCategoryName(subCategoryDto.getSubCategoryName());
        SubCategory savelist = this.subCategoryRepo.save(subCategory);
        return this.subCategoryToDto(savelist);
    }

    @Override
    public List<SubCategoryDto> getAllSubCategory() {
        List<SubCategory> subCategories = this.subCategoryRepo.findAll();
        List<SubCategoryDto> subCategoryDtos = subCategories.stream().map(subCategory -> this.subCategoryToDto(subCategory)).collect(Collectors.toList());
        return subCategoryDtos;
    }

    @Override
    public void deleteSubCategory(Integer subCatId) {
        SubCategory subCategory = this.subCategoryRepo.findById(subCatId).orElseThrow(() -> new ResourceNotFoundException("SubCategory","Id",subCatId));
        this.subCategoryRepo.delete(subCategory);
    }

    public SubCategory dtoToSubcategory(SubCategoryDto subCategoryDto)
    {
        SubCategory subCategory = this.modelMapper.map(subCategoryDto,SubCategory.class);
        return subCategory;
    }

    public SubCategoryDto subCategoryToDto(SubCategory subCategory)
    {
        SubCategoryDto subCategoryDto = this.modelMapper.map(subCategory,SubCategoryDto.class);
        return subCategoryDto;
    }

}
