package com.auction.Controller;

import com.auction.payload.ApiResponse;
import com.auction.payload.SubCategoryDto;
import com.auction.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("admin/createSubCategory/{catId}")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto,@PathVariable Integer catId)
    {
        SubCategoryDto createSubCategory = this.subCategoryService.createSubCategory(subCategoryDto,catId);
        return new ResponseEntity<>(createSubCategory, HttpStatus.CREATED);
    }

    @PutMapping("admin/updateSubCategory/{subcatId}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@RequestBody SubCategoryDto subCategoryDto, @PathVariable Integer subcatId)
    {
        SubCategoryDto updateSubCategory = this.subCategoryService.updateSubCategory(subCategoryDto,subcatId);
        return new ResponseEntity<>(updateSubCategory,HttpStatus.OK);
    }

    @GetMapping("user/getAllSubCategory")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategory()
    {
        return ResponseEntity.ok(this.subCategoryService.getAllSubCategory());
    }

    @DeleteMapping("admin/deleteSubCategory/{subcatId}")
    public ResponseEntity<ApiResponse> deleteSubCategory(@PathVariable Integer subcatId)
    {
        this.subCategoryService.deleteSubCategory(subcatId);
        return new ResponseEntity<>(new ApiResponse("SubCategory delete successfully",true),HttpStatus.OK);
    }
}
