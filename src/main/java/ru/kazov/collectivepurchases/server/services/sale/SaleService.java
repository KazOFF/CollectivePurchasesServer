package ru.kazov.collectivepurchases.server.services.sale;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.SaleNotFoundException;
import ru.kazov.collectivepurchases.server.common.exceptions.UserNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.User;
import ru.kazov.collectivepurchases.server.models.dao.UserRole;
import ru.kazov.collectivepurchases.server.models.dao.sale.Sale;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.repositories.SaleRepository;
import ru.kazov.collectivepurchases.server.repositories.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaleService {

    private final EntityManager entityManager;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<Sale> getSales() {
        User user = getCurrentUser();
        List<Sale> sales;
        if (user.getRole() == UserRole.USER) {
            sales = saleRepository.findAllByActiveIsTrueOrderByEndDateDesc();
        } else {
            sales = saleRepository.findAllByOrderByEndDateDesc();
        }
        return sales;
    }

    @Transactional(readOnly = true)
    public Sale getSale(Long saleId) {
        User user = getCurrentUser();
        Sale sale;
        if (user.getRole() == UserRole.USER) {
            sale = saleRepository.findByIdAndActiveTrue(saleId).orElseThrow(SaleNotFoundException::new);
        } else {
            sale = saleRepository.findById(saleId).orElseThrow(SaleNotFoundException::new);
        }
        return sale;
    }

    @Transactional
    public Long createSale(Sale sale) {
        User user = entityManager.merge(getCurrentUser());
        sale.setOwner(user);

        SaleCategory saleCategory = new SaleCategory();
        saleCategory.setName("Основная категория");
        sale.addCategory(saleCategory);

        saleRepository.saveAndFlush(sale);
        return sale.getId();
    }

    @Transactional
    public void updateSale(Long saleId, Sale sale) {
        Sale persistSale = saleRepository.findById(saleId).orElseThrow(SaleNotFoundException::new);
        persistSale.setName(sale.getName());
        persistSale.setCountry(sale.getCountry());
        persistSale.setActive(sale.isActive());
        persistSale.setPicture(sale.getPicture());
        persistSale.setStartDate(sale.getStartDate());
        persistSale.setEndDate(sale.getEndDate());

        if (sale.getOwner() != null) {
            User user = userRepository.findById(sale.getOwner().getId()).orElseThrow(UserNotFoundException::new);
            persistSale.setOwner(user);
        }
        saleRepository.saveAndFlush(persistSale);
    }

    @Transactional
    public void removeSale(Long saleId) {
        saleRepository.deleteById(saleId);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
