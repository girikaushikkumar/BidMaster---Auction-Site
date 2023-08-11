package com.auction.Controller;

import com.auction.exception.BiddinException.InvalidBidAmountException;
import com.auction.exception.BiddinException.ProductNotBiddableException;
import com.auction.exception.BiddinException.ProductNotFoundException;
import com.auction.payload.BidDto;
import com.auction.payload.GetInfoAboutBiddingOfBidder;
import com.auction.payload.GetInfoAboutBiddingOfSeller;
import com.auction.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class BidController {
    @Autowired
    private BidService bidService;

    @PostMapping("bidder/placebid/{productId}/{bidderId}")
    public ResponseEntity<String> placeBid(@RequestBody BidDto bidDto,@PathVariable int productId, @PathVariable int bidderId) {

        try {
            bidService.placeBid(bidDto,bidderId, productId);
            return ResponseEntity.ok("Bid placed successfully");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        } catch (ProductNotBiddableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not available for bidding");
        } catch (InvalidBidAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bid amount must be higher than the current highest bid");
        }
    }

    @GetMapping("bidder/getBidInfo/{bidderId}")
    public ResponseEntity<List<GetInfoAboutBiddingOfBidder>> getInfoAboutBiddinForBidder(@PathVariable int bidderId) {
        List<GetInfoAboutBiddingOfBidder> infos = this.bidService.getBiddingInformationForBidder(bidderId);
        return new ResponseEntity<>(infos,HttpStatus.OK);
    }

    @GetMapping("seller/getBidInfoOfSeller/{sellerId}")
    public ResponseEntity<List<GetInfoAboutBiddingOfSeller>> getInfoAboutBiddinForSeller(@PathVariable int sellerId) {
        List<GetInfoAboutBiddingOfSeller> infos = this.bidService.getBiddingInformationForSeller(sellerId);
        return new ResponseEntity<>(infos,HttpStatus.OK);
    }

    @GetMapping("bidder/checkWinner/{bidderId}/{productId}")
    public ResponseEntity<String> checkWinnerOrNot(@PathVariable Integer bidderId,@PathVariable Integer productId) {
        String message = this.bidService.CheckWinnerOrNot(bidderId,productId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("seller/checkBiddingStatusBySeller/{productId}")
    public ResponseEntity<String> checkBiddingStatus(@PathVariable Integer productId) {
        String message = this.bidService.checkBiddingStatus(productId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}
