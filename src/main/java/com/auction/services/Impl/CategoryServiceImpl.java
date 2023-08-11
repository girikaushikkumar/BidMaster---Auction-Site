package com.auction.services.Impl;

import com.auction.Entities.Category;
import com.auction.exception.ResourceNotFoundException;
import com.auction.payload.CategoryDto;
import com.auction.repositories.CategoryRepo;
import com.auction.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.dtoToCategory(categoryDto);
        Category savelist = this.categoryRepo.save(category);
        return this.categoryToDto(savelist);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catId) {
        Category category = this.categoryRepo.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category","Id",catId));
        category.setCatName(categoryDto.getCatName());
        Category savelist = this.categoryRepo.save(category);
        return this.categoryToDto(savelist);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = this.categoryRepo.findAll();
        List<CategoryDto> categoryDto = categories.stream().map(category -> this.categoryToDto(category)).collect(Collectors.toList());
        return categoryDto;
    }

    @Override
    public void deleteCategory(Integer catId) {
        Category category = this.categoryRepo.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category","Id",catId));
        this.categoryRepo.delete(category);
    }

    public Category dtoToCategory(CategoryDto categoryDto)
    {
        Category category = this.modelMapper.map(categoryDto,Category.class);
        return category;
    }

    public CategoryDto categoryToDto(Category category)
    {
        CategoryDto categoryDto = this.modelMapper.map(category,CategoryDto.class);
        return categoryDto;
    }
}
