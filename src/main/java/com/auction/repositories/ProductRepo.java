package com.auction.repositories;

import com.auction.Entities.Category;
import com.auction.Entities.Product;
import com.auction.Entities.SubCategory;
import com.auction.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<Product> findByUser(User user);
    List<Product> findByCategory(Category category);
    List<Product> findBySubCategory(SubCategory subCategory);

    List<Product> findByproductNameContaining(String productName);
}
