package com.auction.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetInfoAboutBiddingOfSeller {
    private int sellerId;
    private ProductDto productDto;
    private double UserBidPrice;
    private String timeLeft;
}
