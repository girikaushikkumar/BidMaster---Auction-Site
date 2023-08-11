package com.auction.services.Impl;

import com.auction.Entities.Bid;

import com.auction.Entities.Product;
import com.auction.Entities.User;
import com.auction.exception.BiddinException.InvalidBidAmountException;
import com.auction.exception.BiddinException.ProductNotBiddableException;
import com.auction.exception.BiddinException.ProductNotFoundException;
import com.auction.payload.BidDto;
import com.auction.payload.GetInfoAboutBiddingOfBidder;
import com.auction.payload.GetInfoAboutBiddingOfSeller;
import com.auction.payload.ProductDto;
import com.auction.repositories.BidRepo;
import com.auction.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private UserServiesImpl userServies;
    @Override
    public void placeBid(BidDto bidDto,Integer bidderId, Integer productId) {
        // Check if the product exists and is open for bidding
        double bidAmount = bidDto.getBidAmount();
        ProductDto productDto = productService.getProcutById(productId);
        Product product = productService.dtoToProduct(productDto);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(product.getTimeStart()) || now.isAfter(product.getTimeEnd())) {
            throw new ProductNotBiddableException("Product is not available for bidding");
        }

        // Check if the bid amount is higher than the current highest bid

        if (product != null && bidAmount <= product.getPrice()) {
            throw new InvalidBidAmountException("Bid amount must be higher than the current highest bid");
        }

        Bid highestBid = bidRepo.findHighestBidByProductId(productId);
        if (highestBid != null && bidAmount <= highestBid.getBidAmount()) {
            throw new InvalidBidAmountException("Bid amount must be higher than the current highest bid");
        }

        User user = userServies.dtoToUser(userServies.getUserById(bidderId));
        // Save the new bid
        Bid bid = new Bid();
        bid.setProduct(product);
        bid.setBidId(bidDto.getBidId());
        bid.setBidAmount(bidAmount);
        bid.setBidTime(now);
        bid.setUser(user);

        bidRepo.save(bid);
    }

    public static String calculateTimeDifference(String endTime) {
        // Create a custom DateTimeFormatter without a space between date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");

        // Parse the end time string into a LocalDateTime object using the custom formatter
        LocalDateTime endTimeDateTime = LocalDateTime.parse(endTime, formatter);

        // Rest of the code remains the same
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, endTimeDateTime);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        String formattedDuration = String.format("%ddays %02dhours %02dminutes %02dsecond", days, hours, minutes, seconds);

        return formattedDuration;
    }



    @Override
    public List<GetInfoAboutBiddingOfBidder> getBiddingInformationForBidder(Integer bidderId) {
        List<Bid> bids = bidRepo.findByUserId(bidderId);
        List<GetInfoAboutBiddingOfBidder> infos = new ArrayList<>();
        for(Bid bid : bids)
        {
            GetInfoAboutBiddingOfBidder info = new GetInfoAboutBiddingOfBidder();
            info.setBidderId(bid.getUser().getId());
            info.setProductInfo(bid.getProduct().getProductInfo());
            info.setProductName(bid.getProduct().getProductName());
            info.setProductBrand(bid.getProduct().getBrand());
            info.setUserBidPrice(bid.getBidAmount());
            Bid highestBid = bidRepo.findHighestBidByProductId(bid.getProduct().getProductId());
            info.setCurrentPrice(highestBid.getBidAmount());

            LocalDateTime endTime = bid.getProduct().getTimeEnd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
            String formattedDateTime = endTime.format(formatter);
            String timeLeft = calculateTimeDifference(formattedDateTime);


            info.setTimeLeft(timeLeft);

            infos.add(info);

        }
        return infos;
    }

    @Override
    public List<GetInfoAboutBiddingOfSeller> getBiddingInformationForSeller(Integer sellerId) {
        List<ProductDto> productDtos = productService.getProductBySeller(sellerId);
        List<GetInfoAboutBiddingOfSeller> infos = new ArrayList<>();
        List<Bid> bids = bidRepo.findAll();

        for(ProductDto productDto : productDtos)
        {
            for(Bid bid : bids) {
                if(productDto.getProductId() == bid.getProduct().getProductId()){
                    GetInfoAboutBiddingOfSeller info = new GetInfoAboutBiddingOfSeller();
                    info.setSellerId(sellerId);
                    info.setProductDto(productDto);
                    Bid highestBid = bidRepo.findHighestBidByProductId(bid.getProduct().getProductId());
                    info.setUserBidPrice(highestBid.getBidAmount());

                    LocalDateTime endTime = bid.getProduct().getTimeEnd();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                    String formattedDateTime = endTime.format(formatter);
                    String timeLeft = calculateTimeDifference(formattedDateTime);


                    info.setTimeLeft(timeLeft);

                    infos.add(info);

                }
            }
        }
        return infos;
    }

    @Override
    public String CheckWinnerOrNot(Integer bidderId,Integer productId) {

        ProductDto productDto = productService.getProcutById(productId);
        Product product = productService.dtoToProduct(productDto);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(product.getTimeEnd())) {
            LocalDateTime endTime = product.getTimeEnd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
            String formattedDateTime = endTime.format(formatter);
            String timeLeft = calculateTimeDifference(formattedDateTime);
            String message = "Bidding is ongoing!!! just wait for "+timeLeft;
            return message;
        }


        Bid heighestPrice = bidRepo.findHighestBidByProductId(productId);

        if(bidderId == heighestPrice.getUser().getId()) {
            String message = "Congratulations! You have won the auction for " + heighestPrice.getProduct().getProductName() +
                    " Your winning bid amount is " + heighestPrice.getBidAmount() + " Thank you for participating in the auction.";
            return message;
        } else {
            String msg = "Thank you for participating in the auction for" + heighestPrice.getProduct().getProductName() +
                    " Unfortunately, you were not the winning bidder this time. Better luck next time!";
            return msg;
        }
    }

    @Override
    public String checkBiddingStatus(Integer productId) {
        ProductDto productDto = productService.getProcutById(productId);
        Product product = productService.dtoToProduct(productDto);
        LocalDateTime now = LocalDateTime.now();
        Bid heighestPrice = bidRepo.findHighestBidByProductId(productId);
        if (now.isBefore(product.getTimeEnd())) {
            LocalDateTime endTime = product.getTimeEnd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
            String formattedDateTime = endTime.format(formatter);
            String timeLeft = calculateTimeDifference(formattedDateTime);
            String message = "Bidding is ongoing!!! just wait for "+timeLeft+"  Current Price is "+heighestPrice.getBidAmount();
            return message;
        }
        String message = "Bidding is compeleted."+" Final Price is " + heighestPrice.getBidAmount();
        return message;
    }


}
