package com.auction.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BidDto {
    private int bidId;
    private double bidAmount;

}
