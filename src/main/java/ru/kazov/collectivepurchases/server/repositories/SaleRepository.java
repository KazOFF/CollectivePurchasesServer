package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.sale.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByIdAndActiveTrue(Long saleId);

    List<Sale> findAllByActiveIsTrueOrderByEndDateDesc();
    List<Sale> findAllByOrderByEndDateDesc();
}