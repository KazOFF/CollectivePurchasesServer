package ru.kazov.collectivepurchases.server.services.sale;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.CategoryNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.ItemNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.User;
import ru.kazov.collectivepurchases.server.models.dao.UserRole;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleItem;
import ru.kazov.collectivepurchases.server.repositories.SaleCategoryRepository;
import ru.kazov.collectivepurchases.server.repositories.SaleItemRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaleItemService {

    private final SaleCategoryRepository saleCategoryRepository;
    private final SaleItemRepository saleItemRepository;

    @Transactional( readOnly = true)
    public List<SaleItem> getSaleItems(Long saleCategoryId) {
        User user = getCurrentUser();
        List<SaleItem> saleItems;
        if (user.getRole() == UserRole.USER) {
            saleItems = saleItemRepository.findAllByCategoryIdAndCategorySaleActiveTrue(saleCategoryId);
        } else {
            saleItems = saleItemRepository.findAllByCategoryId(saleCategoryId);
        }
        return saleItems;
    }

    @Transactional(readOnly = true)
    public SaleItem getSaleItem(Long saleItemId) {
        User user = getCurrentUser();
        SaleItem saleItem;
        if (user.getRole() == UserRole.USER) {
            saleItem = saleItemRepository.findByIdAndCategorySaleActiveTrue(saleItemId).orElseThrow(ItemNotFoundException::new);
        } else {
            saleItem = saleItemRepository.findById(saleItemId).orElseThrow(ItemNotFoundException::new);
        }
        return saleItem;
    }

    @Transactional
    public Long createSaleItem(SaleItem saleItem, Long saleCategoryId) {
        SaleCategory saleCategory = saleCategoryRepository.findById(saleCategoryId).orElseThrow(CategoryNotFoundException::new);
        saleItem.setCategory(saleCategory);
        saleItemRepository.saveAndFlush(saleItem);
        return saleItem.getId();
    }

    @Transactional
    public void updateSaleItem(SaleItem saleItem, Long saleItemId) {
        SaleItem persistSaleItem = saleItemRepository.findById(saleItemId).orElseThrow(ItemNotFoundException::new);
        persistSaleItem.setName(saleItem.getName());
        persistSaleItem.setDescription(saleItem.getDescription());
        persistSaleItem.setUrl(saleItem.getUrl());

        if (saleItem.getCategory() != null) {
            SaleCategory category = saleCategoryRepository.findById(saleItem.getCategory().getId()).orElseThrow(CategoryNotFoundException::new);
            persistSaleItem.setCategory(category);
        }

        if (saleItem.getPictures() != null) {
            persistSaleItem.setPictures(saleItem.getPictures());
        }

        if (saleItem.getPrices() != null) {
            persistSaleItem.setPrices(saleItem.getPrices());
        }

        if (saleItem.getProperties() != null) {
            persistSaleItem.setProperties(saleItem.getProperties());
        }

        saleItemRepository.saveAndFlush(saleItem);
    }

    @Transactional
    public void removeSaleItem(Long saleItemId) {
        saleItemRepository.deleteById(saleItemId);
    }

    @Transactional
    public void batchRemoveSaleItems(List<Long> parserItemIdList){
        saleItemRepository.deleteByIdIn(parserItemIdList);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
