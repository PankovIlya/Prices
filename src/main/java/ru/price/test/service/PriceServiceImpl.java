package ru.price.test.service;

import ru.price.test.dto.PriceDto;
import ru.price.test.dto.PriceType;
import ru.price.test.dto.PriceWrapperDto;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.max;
import static java.lang.Long.min;
import static ru.price.test.dto.PriceType.NEW;
import static ru.price.test.dto.PriceType.OLD;

/**
 * Created by pankov on 11.10.2017.
 * PriceService реелизация сервиса слияния списка цен
 */
public class PriceServiceImpl implements PriceService {


    /**
     * Слияние двух списков с разными productCode, number, depart;
     * O(n*log(n))
     * @param oldPrice список старых цен
     * @param newPrice список новых цен
     * @return List<PriceDto> окончательный список цен
     */
    @Override
    public List<PriceDto> mergePrices(List<PriceDto> oldPrice, List<PriceDto> newPrice) {
        HashMap<String, List<PriceDto>> oldMap = createMap(oldPrice);
        HashMap<String, List<PriceDto>> newMap = createMap(newPrice);

        List<PriceDto> result = oldMap.entrySet().stream()
                .flatMap(s -> merge(s.getValue(), newMap.getOrDefault(s.getKey(), new ArrayList<>())).stream())
                .collect(Collectors.toList());
        result.addAll(
                newMap.entrySet().stream()
                        .flatMap(s-> !oldMap.containsKey(s.getKey())? s.getValue().stream(): Stream.empty())
                        .collect(Collectors.toList())
        );

        return result;
    }


    /**
     * Слияние двух списков с равными productCode, number, depart;
     * * O(m*log(m)) m принадлежит n и m <= n
     * @param _oldPrice список старых цен
     * @param _newPrice список новых цен
     * @return List<PriceDto> окончательный список цен
     */
    @Override
    public List<PriceDto> merge(List<PriceDto> _oldPrice, List<PriceDto> _newPrice) {
        LinkedList<PriceWrapperDto> mutual = mutualList(_oldPrice, _newPrice);
        LinkedList<PriceWrapperDto> result = new LinkedList<>();
        PriceWrapperDto now = pop(mutual);
        PriceWrapperDto current = last(result);
        while ( now != null) {

            //первый элемент в списке
            if (current == null || now.getType().equals(current.getType()))
                result.add(now);
            else {
                // если цены равны увеличиваем интервал
                if (now.getPrice().getValue() == current.getPrice().getValue()) {
                    current.getPrice().setBegin(new Date(min(current.getPrice().getBegin().getTime(), now.getPrice().getBegin().getTime())));
                    current.getPrice().setEnd(new Date(max(current.getPrice().getEnd().getTime(), now.getPrice().getEnd().getTime())));
                } else {
                    //текущий интервал новый
                    if (now.getType().equals(NEW)){
                    //end действия старой цены больше end действия новой
                    int compare = current.getPrice().getEnd().compareTo(now.getPrice().getEnd());
                    if ( compare == 1) {
                        //восращаем остаток в список
                        mutual.addFirst(new PriceWrapperDto(current, now.getPrice().getEnd()));
                    }

                    compare = current.getPrice().getBegin().compareTo(now.getPrice().getBegin());
                    //begin действия старой цены больше или равен begin действия новой
                    if ( compare >= 0 ) {
                        //старая цена недействительна
                        result.removeLast();
                    } else
                        //устанавливаем у старой цены период окончания равный началу действия новой
                        current.getPrice().setEnd(now.getPrice().getBegin());

                    result.add(now);
                    } else {
                        //end действия старой цены больше end действия новой
                        int compare = current.getPrice().getEnd().compareTo(now.getPrice().getEnd());
                        if ( compare == -1) {
                            now.getPrice().setBegin(current.getPrice().getEnd());
                            result.add(now);
                        }
                    }
                }
            }
            now = pop(mutual);
            current = last(result);
        }

        return result.stream().map(PriceWrapperDto::getPrice).collect(Collectors.toList());
    }

    /**
     * создает LinkedList содержащий старые и новые цены и отсортированный по дате начала действия
     * @param _oldPrice список старых цен
     * @param _newPrice список новых цен
     * @return LinkedList<PriceWrapperDto>
     */
    private   LinkedList<PriceWrapperDto> mutualList(List<PriceDto> _oldPrice, List<PriceDto> _newPrice) {
        List<PriceWrapperDto> sharedList = new ArrayList<>();
        sharedList.addAll(_oldPrice.stream().map(p-> new PriceWrapperDto(OLD, p)).collect(Collectors.toList()));
        sharedList.addAll(_newPrice.stream().map(p-> new PriceWrapperDto(NEW, p)).collect(Collectors.toList()));

        LinkedList<PriceWrapperDto> linkedList = new LinkedList<>();
        linkedList.addAll(
                sharedList.stream()
                        .sorted(Comparator.comparing(p -> p.getPrice().getBegin()))
                        .collect(Collectors.toList()));
        return linkedList;
    }

    private PriceWrapperDto last(LinkedList<PriceWrapperDto> list) {
        if (!list.isEmpty())
            return list.getLast();
        return null;
    }

    private PriceWrapperDto pop(LinkedList<PriceWrapperDto> list){
        if (!list.isEmpty())
            return list.pop();
        return null;
    }

    private HashMap<String, List<PriceDto>> createMap(List<PriceDto> prices){
        HashMap<String, List<PriceDto>> map = new HashMap<>();
        prices.forEach(p-> {
            List<PriceDto> localList =  map.getOrDefault(p.getKey(), new ArrayList<>());
            localList.add(p);
            map.put(p.getKey(), localList);
        });
        return map;

    }

}
