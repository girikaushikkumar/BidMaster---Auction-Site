package com.auction.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetInfoAboutBiddingOfBidder {
    private int bidderId;
    private String productName;
    private String productInfo;
    private String productBrand;
    private double currentPrice;
    private double UserBidPrice;
    private String timeLeft;
}
