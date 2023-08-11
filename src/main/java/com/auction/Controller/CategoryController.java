package com.auction.Controller;

import com.auction.payload.ApiResponse;
import com.auction.payload.CategoryDto;
import com.auction.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("admin/createCategory")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto)
    {
        CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("admin/updateCategory/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Integer catId)
    {
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto,catId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("user/getCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory()
    {
        return ResponseEntity.ok(this.categoryService.getAllCategory());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/deleteCategory/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId)
    {
        this.categoryService.deleteCategory(catId);
        return new ResponseEntity<>(new ApiResponse("Category deleted successfully",true),HttpStatus.OK);
    }

}
