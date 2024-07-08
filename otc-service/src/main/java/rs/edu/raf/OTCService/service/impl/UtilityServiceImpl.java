package rs.edu.raf.OTCService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.entity.listing.BankOTCStock;
import rs.edu.raf.OTCService.data.entity.offer.Offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilityServiceImpl {

    public Offer findOfferWithHighestPrice(List<Offer> offerList) {
        if (offerList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Offer highestPriceOffer = offerList.get(0);
        for (int i = 1; i < offerList.size(); i++) {
            Offer currentOffer = offerList.get(i);
            if (currentOffer.getPrice() > highestPriceOffer.getPrice()) {
                highestPriceOffer = currentOffer;
            }
        }
        return highestPriceOffer;
    }

    public Offer findOfferWithLowestPrice(List<Offer> offerList) {
        if (offerList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Offer lowestPriceOffer = offerList.get(0);
        for (int i = 1; i < offerList.size(); i++) {
            Offer currentOffer = offerList.get(i);
            if (currentOffer.getPrice() < lowestPriceOffer.getPrice()) {
                lowestPriceOffer = currentOffer;
            }
        }
        return lowestPriceOffer;
    }

    public BankOTCStock findStockWithHighestAmount(List<BankOTCStock> bankOTCStocks) {
        BankOTCStock highestAmountStock = bankOTCStocks.get(0);
        for (int i = 1; i < bankOTCStocks.size(); i++) {
            BankOTCStock currentStock = bankOTCStocks.get(i);
            if (currentStock.getAmount() > highestAmountStock.getAmount()) {
                highestAmountStock = currentStock;
            }
        }
        return highestAmountStock;
    }

    public String generateAccountNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public List<String> filterEmails(List<String> emails, String domain) {
        return emails.stream()
                .filter(email -> email.endsWith("@" + domain))
                .collect(Collectors.toList());
    }

    public List<Integer> findPrimeNumbers(int limit) {
        List<Integer> primeNumbers = new ArrayList<>();

        for (int num = 2; num <= limit; num++) {
            boolean isPrime = true;
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primeNumbers.add(num);
            }
        }

        return primeNumbers;
    }

    public String reverseString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string must not be null");
        }
        return new StringBuilder(input).reverse().toString();
    }
}