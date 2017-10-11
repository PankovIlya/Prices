package price;


import org.junit.Test;
import ru.price.test.dto.PriceDto;
import ru.price.test.service.PriceServiceImpl;
import ru.price.test.service.PriceService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author i.e.pankov
 */
public class TestPriceService {
    private PriceService priceService = new PriceServiceImpl();


    @Test
    public void testSimpleMergePrices() throws Exception {
        int priceCount = 6;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(60000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(60000), new Date(180000), 500),
                new PriceDto(0L, "A1", 1, 1, new Date(180000), new Date(240000), 1000)).collect(Collectors.toList());

        List<PriceDto> newPrice = Stream.of(
                new PriceDto(3L, "A2", 1, 1, new Date(240000), new Date(300000), 1500),
                new PriceDto(0L, "A2", 1, 1, new Date(300000), new Date(360000), 1000),
                new PriceDto(4L, "A2", 1, 1, new Date(360000), new Date(420000), 2000)).collect(Collectors.toList());


        List<PriceDto> result = priceService.mergePrices(oldPrice, newPrice);
        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(60000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(60000), new Date(180000), 500),
                new PriceDto(0L, "A1", 1, 1, new Date(180000), new Date(240000), 1000),
                new PriceDto(3L, "A2", 1, 1, new Date(240000), new Date(300000), 1500),
                new PriceDto(0L, "A2", 1, 1, new Date(300000), new Date(360000), 1000),
                new PriceDto(4L, "A2", 1, 1, new Date(360000), new Date(420000), 2000)).collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());

    }

    @Test
    public void testMergePrices() throws Exception {
        int priceCount = 6;


        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(600000), 1000),
                new PriceDto(3L, "A2", 1, 1, new Date(0), new Date(300000), 1000),
                new PriceDto(4L, "A2", 1, 1, new Date(300000), new Date(600000), 1500))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(180000), new Date(420000), 500),
                new PriceDto(5L, "A2", 1, 1, new Date(150000), new Date(450000), 500))
                .collect(Collectors.toList());


        List<PriceDto> result = priceService.mergePrices(oldPrice, newPrice).stream()
                .sorted((p1, p2) -> p1.getProductCode().equals(p2.getProductCode())? p1.getBegin().compareTo(p2.getBegin()): p1.getProductCode().compareTo(p2.getProductCode()) )
                .collect(Collectors.toList());
        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(180000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(180000), new Date(420000), 500),
                new PriceDto(0L, "A1", 1, 1, new Date(420000), new Date(600000), 1000),
                new PriceDto(3L, "A2", 1, 1, new Date(0), new Date(150000), 1000),
                new PriceDto(5L, "A2", 1, 1, new Date(150000), new Date(450000), 500),
                new PriceDto(4L, "A2", 1, 1, new Date(450000), new Date(600000), 1500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());

    }



    @Test
    public void testMergeOnlyNew() throws Exception {
        int priceCount = 1;
        List<PriceDto> oldPrice = Collections.emptyList();
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(420000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(420000), 500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeOnlyOld() throws Exception {
        int priceCount = 1;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(300000), new Date(600000), 500))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Collections.emptyList();

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(300000), new Date(600000), 500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }



    @Test
    public void testMergeFusionBefore() throws Exception {
        int priceCount = 1;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(300000), new Date(600000), 500))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(420000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(600000), 500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeFusionAfter() throws Exception {
        int priceCount = 1;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(420000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(300000), new Date(600000), 1000))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(600000), 1000))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeBeforeMore() throws Exception {
        int priceCount = 2;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(300000), new Date(600000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(420000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(420000), 500),
                new PriceDto(1L, "A1", 1, 1, new Date(420000), new Date(600000), 1000))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeAfterLess() throws Exception {
        int priceCount = 2;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(420000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(300000), new Date(600000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(300000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(300000), new Date(600000), 500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeSimpleSplitting() throws Exception {
        int priceCount = 3;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(600000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(180000), new Date(420000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(180000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(180000), new Date(420000), 500),
                new PriceDto(0L, "A1", 1, 1, new Date(420000), new Date(600000), 1000))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeMultipleSplitting() throws Exception {
        int priceCount = 7;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(600000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(60000), new Date(180000), 500),
                new PriceDto(3L, "A1", 1, 1, new Date(240000), new Date(300000), 1500),
                new PriceDto(4L, "A1", 1, 1, new Date(360000), new Date(420000), 2000))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(60000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(60000), new Date(180000), 500),
                new PriceDto(0L, "A1", 1, 1, new Date(180000), new Date(240000), 1000),
                new PriceDto(3L, "A1", 1, 1, new Date(240000), new Date(300000), 1500),
                new PriceDto(0L, "A1", 1, 1, new Date(300000), new Date(360000), 1000),
                new PriceDto(4L, "A1", 1, 1, new Date(360000), new Date(420000), 2000),
                new PriceDto(0L, "A1", 1, 1, new Date(420000), new Date(600000), 1000))

                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeInsert() throws Exception {
        int priceCount = 3;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(300000), 1000),
                new PriceDto(2L, "A1", 1, 1, new Date(300000), new Date(600000), 1500))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(3L, "A1", 1, 1, new Date(150000), new Date(450000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(0), new Date(150000), 1000),
                new PriceDto(3L, "A1", 1, 1, new Date(150000), new Date(450000), 500),
                new PriceDto(2L, "A1", 1, 1, new Date(450000), new Date(600000), 1500))
                .collect(Collectors.toList());
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeAbsorption() throws Exception {
        int priceCount = 1;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(120000), new Date(420000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(600000), 500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(600000), 500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeTwoAbsorption() throws Exception {
        int priceCount = 2;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(120000), new Date(420000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(300000), 500),
                new PriceDto(3L, "A1", 1, 1, new Date(300000), new Date(600000), 1500))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(300000), 500),
                new PriceDto(3L, "A1", 1, 1, new Date(300000), new Date(600000), 1500))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testMergeMultipleAbsorption() throws Exception {
        int priceCount = 3;
        List<PriceDto> oldPrice = Stream.of(
                new PriceDto(1L, "A1", 1, 1, new Date(120000), new Date(480000), 1000))
                .collect(Collectors.toList());
        List<PriceDto> newPrice = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(180000), 500),
                new PriceDto(3L, "A1", 1, 1, new Date(180000), new Date(300000), 1500),
                new PriceDto(4L, "A1", 1, 1, new Date(300000), new Date(600000), 2000))
                .collect(Collectors.toList());

        List<PriceDto> result = priceService.merge(oldPrice, newPrice);

        System.out.println(result);
        assertNotNull(result);
        assertEquals(priceCount, result.size());

        List<PriceDto> expected = Stream.of(
                new PriceDto(2L, "A1", 1, 1, new Date(0), new Date(180000), 500),
                new PriceDto(3L, "A1", 1, 1, new Date(180000), new Date(300000), 1500),
                new PriceDto(4L, "A1", 1, 1, new Date(300000), new Date(600000), 2000))
                .collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), result.toArray());
    }
}

