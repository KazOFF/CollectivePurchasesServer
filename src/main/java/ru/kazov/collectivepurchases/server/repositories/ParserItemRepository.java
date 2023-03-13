package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserItem;

import java.util.Collection;
import java.util.List;

public interface ParserItemRepository extends JpaRepository<ParserItem, Long> {
    void deleteByIdIn(Collection<Long> ids);

    List<ParserItem> findAllByIdIn(Collection<Long> ids);
    List<ParserItem> findAllByCategoryId(Long parserCategoryId);
}