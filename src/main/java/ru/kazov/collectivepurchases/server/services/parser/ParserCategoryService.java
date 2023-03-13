package ru.kazov.collectivepurchases.server.services.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.CategoryNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserCategory;
import ru.kazov.collectivepurchases.server.repositories.ParserCategoryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ParserCategoryService {

    private final ParserCategoryRepository parserCategoryRepository;

    @Transactional(readOnly = true)
    public List<ParserCategory> getParserCategories() {
        return parserCategoryRepository.findAllByOrderByDateDesc();
    }

    @Transactional(readOnly = true)
    public ParserCategory getParserCategory(Long parserCategoryId) {
        return parserCategoryRepository.findById(parserCategoryId).orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional
    public Long createParserCategory(ParserCategory parserCategory) {
        parserCategoryRepository.saveAndFlush(parserCategory);
        return parserCategory.getId();
    }

    @Transactional
    public void updateParserCategory(ParserCategory parserCategory, Long parserCategoryId) {
        ParserCategory persistParserCategory = parserCategoryRepository.findById(parserCategoryId).orElseThrow(CategoryNotFoundException::new);
        persistParserCategory.setName(parserCategory.getName());
        persistParserCategory.setDate(parserCategory.getDate());
        parserCategoryRepository.saveAndFlush(persistParserCategory);
    }

    @Transactional
    public void removeParserCategory(Long parserCategoryId) {
        parserCategoryRepository.deleteById(parserCategoryId);
    }
}
