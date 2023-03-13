package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;

public interface ParserShopRepository extends JpaRepository<ParserShop, Long> {
}