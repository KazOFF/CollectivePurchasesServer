package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserCategory;

import java.util.List;

public interface ParserCategoryRepository extends JpaRepository<ParserCategory, Long> {
    List<ParserCategory> findAllByOrderByDateDesc();
}