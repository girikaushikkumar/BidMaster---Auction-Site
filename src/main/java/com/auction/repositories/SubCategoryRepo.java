package com.auction.repositories;

import com.auction.Entities.Category;
import com.auction.Entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepo extends JpaRepository<SubCategory,Integer> {
    SubCategory findBysubCategoryName(String subCategoryName);

}
