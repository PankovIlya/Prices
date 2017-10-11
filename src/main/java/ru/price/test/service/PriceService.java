package ru.price.test.service;

import ru.price.test.dto.PriceDto;

import java.util.List;

/**
 * Created by pankov on 11.10.2017.
 */
public interface PriceService {

    List<PriceDto> mergePrices(List<PriceDto> oldPrice, List<PriceDto> newPrice);

    List<PriceDto> merge(List<PriceDto> oldPrice, List<PriceDto> newPrice);
}
