package com.auction.repositories;

import com.auction.Entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepo extends JpaRepository<Bid,Integer> {
    @Query("SELECT b FROM Bid b WHERE b.product.productId = :productId AND b.bidAmount = (SELECT MAX(b2.bidAmount) FROM Bid b2 WHERE b2.product.productId = :productId)")
    Bid findHighestBidByProductId(int productId);

    List<Bid> findByUserId(int userId);
}
