package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;

import java.util.List;
import java.util.Optional;

public interface SaleCategoryRepository extends JpaRepository<SaleCategory, Long> {

    List<SaleCategory> findAllBySaleIdOrderByName(Long saleId);

    List<SaleCategory> findAllBySaleIdAndSaleActiveTrueOrderByName(Long saleId);

    Optional<SaleCategory> findByIdAndSaleActiveTrue(Long saleCategoryId);
}