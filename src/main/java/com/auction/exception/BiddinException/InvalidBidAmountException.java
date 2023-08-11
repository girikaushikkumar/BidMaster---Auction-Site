package com.auction.exception.BiddinException;

public class InvalidBidAmountException extends RuntimeException {
    public InvalidBidAmountException(String message) {
        super(message);
    }
}

