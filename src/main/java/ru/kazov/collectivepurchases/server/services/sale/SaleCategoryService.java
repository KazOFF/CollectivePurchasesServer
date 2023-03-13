package ru.kazov.collectivepurchases.server.services.sale;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.CategoryNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.SaleNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.User;
import ru.kazov.collectivepurchases.server.models.dao.UserRole;
import ru.kazov.collectivepurchases.server.models.dao.sale.Sale;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.repositories.SaleCategoryRepository;
import ru.kazov.collectivepurchases.server.repositories.SaleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaleCategoryService {

    private final SaleCategoryRepository saleCategoryRepository;
    private final SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public List<SaleCategory> getSaleCategories(Long saleId) {
        User user = getCurrentUser();
        List<SaleCategory> saleCategories;
        if (user.getRole() == UserRole.USER) {
            saleCategories = saleCategoryRepository.findAllBySaleIdAndSaleActiveTrueOrderByName(saleId);
        } else {
            saleCategories = saleCategoryRepository.findAllBySaleIdOrderByName(saleId);
        }
        return saleCategories;
    }

    @Transactional(readOnly = true)
    public SaleCategory getSaleCategory(Long saleCategoryId) {
        User user = getCurrentUser();
        SaleCategory saleCategory;
        if (user.getRole() == UserRole.USER) {
            saleCategory = saleCategoryRepository.findByIdAndSaleActiveTrue(saleCategoryId).orElseThrow(CategoryNotFoundException::new);
        } else {
            saleCategory = saleCategoryRepository.findById(saleCategoryId).orElseThrow(CategoryNotFoundException::new);
        }
        return saleCategory;
    }

    @Transactional
    public Long createSaleCategory(SaleCategory saleCategory, Long saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(SaleNotFoundException::new);
        sale.addCategory(saleCategory);
        saleCategoryRepository.saveAndFlush(saleCategory);
        return saleCategory.getId();
    }

    @Transactional
    public void updateSaleCategory(SaleCategory saleCategory, Long saleCategoryId) {
        SaleCategory persistSaleCategory = saleCategoryRepository.findById(saleCategoryId).orElseThrow(CategoryNotFoundException::new);
        persistSaleCategory.setName(saleCategory.getName());

        if (saleCategory.getSale() != null) {
            Sale sale = saleRepository.findById(saleCategory.getSale().getId()).orElseThrow(SaleNotFoundException::new);
            persistSaleCategory.setSale(sale);
        }
        saleCategoryRepository.saveAndFlush(saleCategory);
    }

    @Transactional
    public void removeSaleCategory(Long saleCategoryId) {
        saleCategoryRepository.deleteById(saleCategoryId);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
