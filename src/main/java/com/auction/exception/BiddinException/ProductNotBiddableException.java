package com.auction.exception.BiddinException;

public class ProductNotBiddableException extends RuntimeException {
    public ProductNotBiddableException(String message) {
        super(message);
    }
}

