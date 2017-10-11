package ru.price.test.dto;

import java.util.Date;

/**
 * Created by pankov on 11.10.2017.
 */
public class PriceWrapperDto {
    private PriceType type;
    private PriceDto price;

    public PriceWrapperDto(PriceType type, PriceDto price) {
        this.type = type;
        this.price = new PriceDto(price);
    }

    public PriceWrapperDto(PriceWrapperDto priceWrapper) {
        this(priceWrapper.getType(), priceWrapper.getPrice());
    }

    public PriceWrapperDto(PriceWrapperDto priceWrapper, Date begin) {
        this.type = priceWrapper.getType();
        this.price = new PriceDto(priceWrapper.getPrice(), begin);
    }

    public PriceWrapperDto(PriceWrapperDto priceWrapper, Date begin, Date end) {
        this.type = priceWrapper.getType();
        this.price = new PriceDto(priceWrapper.getPrice(), begin, end);
    }


    public PriceType getType() {
        return type;
    }

    public void setType(PriceType type) {
        this.type = type;
    }

    public PriceDto getPrice() {
        return price;
    }

    public void setPrice(PriceDto price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PriceWrapperDto{" +
                "type=" + type +
                ", price=" + price +
                '}';
    }
}
