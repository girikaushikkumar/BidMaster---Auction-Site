package com.auction.services.Impl;

import com.auction.Entities.Category;
import com.auction.Entities.Product;

import com.auction.Entities.SubCategory;
import com.auction.Entities.User;
import com.auction.exception.ResourceNotFoundException;
import com.auction.payload.ProductDto;
import com.auction.payload.ProductResponse;
import com.auction.repositories.CategoryRepo;
import com.auction.repositories.ProductRepo;
import com.auction.repositories.UserRepo;
import com.auction.repositories.SubCategoryRepo;
import com.auction.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ProductDto createProduct(ProductDto productDto, Integer sellerId,Integer subCategoryId) {
        User seller = this.userRepo.findById(sellerId).orElseThrow(() ->new ResourceNotFoundException("Seller","Id",sellerId));
        SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("Subcategory","id",subCategoryId));
        Category category = subCategory.getCategory();


        Product product = this.dtoToProduct(productDto);
        LocalDateTime time = LocalDateTime.now();
        product.setTimeStart(time);
        product.setUser(seller);
        product.setCategory(category);
        product.setSubCategory(subCategory);

        Product savelist = this.productRepo.save(product);
        return this.productToDto(savelist);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","Id",productId));
        product.setProductInfo(productDto.getProductInfo());
        product.setProductName(productDto.getProductName());
        product.setBrand(productDto.getBrand());
        product.setImage(productDto.getImage());
        product.setTimeEnd(productDto.getTimeEnd());
        product.setTimeStart(productDto.getTimeStart());
        product.setPrice(productDto.getPrice());

        Product updateProduct = this.productRepo.save(product);
        return this.productToDto(updateProduct);
    }

    @Override
    public ProductDto getProcutById(Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","Id",productId));

        return this.productToDto(product);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products = this.productRepo.findAll();
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());
        return productDtos;
    }

    //Pagination
    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pageProduct = this.productRepo.findAll(pageable);
        List<Product> products = pageProduct.getContent();
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElement(pageProduct.getTotalElements());
        productResponse.setTotalPage(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public void  deleteProduct(Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","Id",productId));
        this.productRepo.delete(product);

    }

    @Override
    public List<ProductDto> getProductBySeller(Integer sellerId) {
        User user = this.userRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller","Id",sellerId));
        List<Product> products = this.productRepo.findByUser(user);
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public List<ProductDto> getProductByCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","Id",categoryId));
        List<Product> products = this.productRepo.findByCategory(category);
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public List<ProductDto> getProductBySubCategory(Integer subCategoryId) {
        SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("Subcategory","id",subCategoryId));
        List<Product> products = this.productRepo.findBySubCategory(subCategory);
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public List<ProductDto> searchProductByProductName(String productName) {
        List<Product> products = this.productRepo.findByproductNameContaining(productName);
        List<ProductDto> productDtos = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());
        return productDtos;
    }

    public ProductDto productToDto(Product product)
    {
        ProductDto productDto = this.modelMapper.map(product,ProductDto.class);
//        ProductDto productDto = new ProductDto();
//        productDto.setProductInfo(product.getProductInfo());
//        productDto.setProductName(product.getProductName());
//        productDto.setPrice(product.getPrice());
//        productDto.setImage(product.getImage());
//        productDto.setBrand(product.getBrand());
//        productDto.setTimeEnd(product.getTimeEnd());
//        productDto.setTimeStart(product.getTimeStart());
//        productDto.setCategory(product.getCategory().getCatName());
//        productDto.setSubCategory(product.getSubCategory().getSubCategoryName());

        return productDto;
    }

    public Product dtoToProduct(ProductDto productDto)
    {
        Product product = this.modelMapper.map(productDto,Product.class);
//        Product product = new Product();
//        product.setProductInfo(productDto.getProductInfo());
//        product.setProductName(productDto.getProductName());
//        product.setBrand(productDto.getBrand());
//        product.setImage(productDto.getImage());
//        product.setTimeEnd(productDto.getTimeEnd());
//        product.setTimeStart(productDto.getTimeStart());
//        product.setPrice(productDto.getPrice());
//        Category category = this.categoryRepo.findBycatName(productDto.getCategory());
//        product.setCategory(category);
//        SubCategory subCategory = this.subCategoryRepo.findbysubCategoryName(productDto.getSubCategory());
//        product.setSubCategory(subCategory);
        return product;
    }

}
