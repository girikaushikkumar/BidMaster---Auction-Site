package com.auction.repositories;

import com.auction.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Category findBycatName(String name);
}
