package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.ListingDto;
import rs.edu.raf.StockService.data.entities.Listing;

@Component
public class ListingMapper {
    public ListingDto listingToListingDto(Listing listing) {
        return new ListingDto(
                listing.getSymbol(),
                listing.getDescription(),
                listing.getExchange(),
                listing.getLastRefresh(),
                listing.getPrice(),
                listing.getHigh(),
                listing.getLow(),
                listing.getChange(),
                listing.getVolume()
        );
    }

    public Listing listingDtoToListing(ListingDto listingDto) {
        return new Listing(
                listingDto.getSymbol(),
                listingDto.getDescription(),
                listingDto.getExchange(),
                listingDto.getLastRefresh(),
                listingDto.getPrice(),
                listingDto.getHigh(),
                listingDto.getLow(),
                listingDto.getChange(),
                listingDto.getVolume()
        );
    }
}
