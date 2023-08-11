package com.auction.services;


import com.auction.Entities.Bid;
import com.auction.payload.BidDto;
import com.auction.payload.GetInfoAboutBiddingOfBidder;
import com.auction.payload.GetInfoAboutBiddingOfSeller;

import java.util.List;

public interface BidService {
    void placeBid(BidDto bidDto,Integer bidderId,Integer productId);

    List<GetInfoAboutBiddingOfBidder> getBiddingInformationForBidder(Integer bidderId);

    List<GetInfoAboutBiddingOfSeller> getBiddingInformationForSeller(Integer sellerId);

    String CheckWinnerOrNot(Integer bidderId,Integer productId);

    String checkBiddingStatus(Integer productId);
}
