package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleItem;

import java.util.List;
import java.util.Optional;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findAllByCategoryId(Long saleCategoryId);

    List<SaleItem> findAllByCategoryIdAndCategorySaleActiveTrue(Long saleCategoryId);

    Optional<SaleItem> findByIdAndCategorySaleActiveTrue(Long saleItemId);

    void deleteByIdIn(List<Long> ids);
}