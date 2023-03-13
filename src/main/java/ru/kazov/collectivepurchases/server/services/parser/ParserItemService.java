package ru.kazov.collectivepurchases.server.services.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.common.exceptions.CategoryNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.ItemNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.JobNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserCategory;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserItem;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleItem;
import ru.kazov.collectivepurchases.server.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParserItemService {

    private final ParserCategoryRepository parserCategoryRepository;
    private final ParserItemRepository parserItemRepository;
    private final ParserJobRepository parserJobRepository;

    private final ObjectMapper objectMapper;
    private final SaleCategoryRepository saleCategoryRepository;
    private final SaleItemRepository saleItemRepository;

    @Transactional(readOnly = true)
    public List<ParserItem> getParserItems(Long parserCategoryId) {
        return parserItemRepository.findAllByCategoryId(parserCategoryId);
    }

    @Transactional(readOnly = true)
    public ParserItem getParserItem(Long parserItemId) {
        return parserItemRepository.findById(parserItemId).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public Long createParserItem(ParserItem parserItem, Long parserCategoryId) {
        ParserCategory parserCategory = parserCategoryRepository.findById(parserCategoryId).orElseThrow(CategoryNotFoundException::new);
        parserItem.setCategory(parserCategory);
        parserItemRepository.saveAndFlush(parserItem);
        return parserItem.getId();
    }

    @Transactional
    public Long createBatchParserItems(List<ParserItem> parserItems, Long parserJobId) {
        ParserJob parserJob = parserJobRepository.findById(parserJobId).orElseThrow(JobNotFoundException::new);
        ParserCategory parserCategory = new ParserCategory();
        parserCategory.setName(parserJob.getShop().getName());
        parserCategory.setDate(new Date());
        parserCategoryRepository.saveAndFlush(parserCategory);
        List<ParserItem> updatedList = parserItems.stream().peek((item) -> item.setCategory(parserCategory)).toList();
        parserItemRepository.saveAllAndFlush(updatedList);
        return parserCategory.getId();
    }

    @Transactional
    public void updateParserItem(ParserItem parserItem, Long parserItemId) {
        ParserItem persistParserItem = parserItemRepository.findById(parserItemId).orElseThrow(ItemNotFoundException::new);
        persistParserItem.setName(parserItem.getName());
        persistParserItem.setDescription(parserItem.getDescription());
        persistParserItem.setUrl(parserItem.getUrl());

        if (parserItem.getCategory() != null) {
            ParserCategory category = parserCategoryRepository.findById(parserItem.getCategory().getId()).orElseThrow(CategoryNotFoundException::new);
            persistParserItem.setCategory(category);
        }

        if (parserItem.getPictures() != null)
            persistParserItem.setPictures(parserItem.getPictures());

        if (parserItem.getPrices() != null)
            persistParserItem.setPrices(parserItem.getPrices());

        if (parserItem.getProperties() != null)
            persistParserItem.setProperties(parserItem.getProperties());

        parserItemRepository.saveAndFlush(persistParserItem);
    }

    @Transactional
    public void removeParserItem(Long parserItemId) {
        parserItemRepository.deleteById(parserItemId);
    }

    @Transactional
    public void batchRemoveParserItems(List<Long> parserItemIdList){
        parserItemRepository.deleteByIdIn(parserItemIdList);
    }

    @Transactional
    public void transferToSale(List<Long> parserItemIdList, Long saleCategoryId, double rate, double scale, String priceComment, int roundPlaces){
        List<ParserItem> parserItemList = parserItemRepository.findAllByIdIn(parserItemIdList);
        SaleCategory saleCategory = saleCategoryRepository.findById(saleCategoryId).orElseThrow(CategoryNotFoundException::new);

        List<SaleItem> saleItemList = parserItemList.stream()
                .map(objectMapper::convertParserItemToSaleItem)
                .peek(item -> {
                    item.setId(null);
                    item.setCategory(saleCategory);
                    item.setPriceComment(priceComment);
                    item.setPictures(new ArrayList<>(item.getPictures()));
                    item.setProperties(new HashMap<>(item.getProperties()));
                    item.setPrices(new HashMap<>(item.getPrices().entrySet().stream()
                            .peek((price) -> price.setValue(round(price.getValue()*rate*scale, roundPlaces)))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
                }).toList();



        saleItemRepository.saveAll(saleItemList);
    }

    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.CEILING);
        return bd.doubleValue();
    }
}
