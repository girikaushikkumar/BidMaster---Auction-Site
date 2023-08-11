package com.auction.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private int productId;

    private String productName;

    private String brand;


    private String productInfo;

    private int price;

    private LocalDateTime  timeEnd;

    private LocalDateTime timeStart = getTimeStart().now();

    private String image;

}
