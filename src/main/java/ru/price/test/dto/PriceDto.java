package ru.price.test.dto;

import java.util.Date;

/**
 * Created by pankov on 11.10.2017.
 */
public class PriceDto {
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public PriceDto(PriceDto price) {
        this.id = price.id;
        this.productCode = price.productCode;
        this.number = price.number;
        this.depart = price.depart;
        this.begin = price.begin;
        this.end = price.end;
        this.value = price.value;

    }

    public PriceDto(PriceDto price, Date begin) {
        this(price);
        this.id = 0;
        this.begin = begin;
    }

    public PriceDto(PriceDto price, Date begin, Date end) {
        this(price);
        this.id = 0;
        this.begin = begin;
        this.end = end;
    }

    public PriceDto(Long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public String getKey(){
        return productCode + '_' + number + '_' +depart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PriceDto{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceDto priceDto = (PriceDto) o;

        if (id != priceDto.id) return false;
        if (number != priceDto.number) return false;
        if (depart != priceDto.depart) return false;
        if (value != priceDto.value) return false;
        if (!productCode.equals(priceDto.productCode)) return false;
        if (!begin.equals(priceDto.begin)) return false;
        return end.equals(priceDto.end);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + productCode.hashCode();
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }
}


