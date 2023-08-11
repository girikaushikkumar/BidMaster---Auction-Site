package com.auction.Controller;

import com.auction.payload.ApiResponse;
import com.auction.payload.ProductDto;
import com.auction.payload.ProductResponse;
import com.auction.services.FileService;
import com.auction.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("$(project.image}")
    private String path;

    @PostMapping("seller/createProduct/{sellerId}/{subCategoryId}")
    public ResponseEntity<ProductDto> crateProduct(@RequestBody ProductDto productDto, @PathVariable Integer sellerId,@PathVariable Integer subCategoryId)
    {
        ProductDto createProduct = this.productService.createProduct(productDto,sellerId,subCategoryId);
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }

    @PutMapping("seller/updateProduct/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,@PathVariable Integer productId)
    {
        ProductDto updateProduct = this.productService.updateProduct(productDto,productId);
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }

    @GetMapping("user/getAllProduct")
    public ResponseEntity<List<ProductDto>> getAllProduc()
    {
        List<ProductDto> productDtos = this.productService.getAllProduct();
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("user/page")
    public ResponseEntity<ProductResponse> getAllProduct(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                        @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy",defaultValue = "productId",required = false) String sortBy)
    {
        ProductResponse productResponse = this.productService.getAllProduct(pageNumber,pageSize,sortBy);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    //Both admin and seller can acess this api
    @DeleteMapping("deleteProduct/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer productId)
    {
        this.productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse("Product delete Successfully",true),HttpStatus.OK);
    }

    @GetMapping("seller/getProductById/{sellerId}")
    public ResponseEntity<List<ProductDto>> getAllProductBySeller(@PathVariable Integer sellerId)
    {
        List<ProductDto> productDtos = this.productService.getProductBySeller(sellerId);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("user/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getAllProductByCategory(@PathVariable Integer categoryId)
    {
        List<ProductDto> productDtos = this.productService.getProductByCategory(categoryId);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("user/subCategory/{subCategoryId}")
    public ResponseEntity<List<ProductDto>> getAllProductBySubCategoryId(@PathVariable Integer subCategoryId)
    {
        List<ProductDto> productDtos = this.productService.getProductBySubCategory(subCategoryId);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("user/search/{productName}")
    public ResponseEntity<List<ProductDto>> searchProductByName(@PathVariable String productName)
    {
        List<ProductDto> productDtos  = this.productService.searchProductByProductName(productName);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    //product image upload
    @PostMapping("seller/image/upload/{productId}")
    public ResponseEntity<ProductDto> upoloadProductImage(@RequestParam("image")MultipartFile image,@PathVariable Integer productId) throws IOException {
        ProductDto productDto = this.productService.getProcutById(productId);
        String fileName = this.fileService.uploadImage(path,image);
        productDto.setImage(fileName);
        ProductDto updateProduct = this.productService.updateProduct(productDto,productId);
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }

    @GetMapping(value = "user/image/{imageName}" ,produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException{
        InputStream resource = this.fileService.getResources(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
