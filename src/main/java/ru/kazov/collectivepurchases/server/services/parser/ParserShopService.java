package ru.kazov.collectivepurchases.server.services.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.common.exceptions.ItemNotFoundException;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;
import ru.kazov.collectivepurchases.server.repositories.ParserShopRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ParserShopService {

    private final ParserShopRepository parserShopRepository;

    @Transactional(readOnly = true)
    public List<ParserShop> getParserShops() {
        return parserShopRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ParserShop getParserShop(Long parserShopId) {
        return parserShopRepository.findById(parserShopId).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public Long createParserShop(ParserShop parserShop) {
        parserShopRepository.saveAndFlush(parserShop);
        return parserShop.getId();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateParserShop(ParserShop parserShop, Long parserShopId) {
        ParserShop persistParserShop = parserShopRepository.findById(parserShopId).orElseThrow(ItemNotFoundException::new);
        persistParserShop.setName(parserShop.getName());
        persistParserShop.setPicture(parserShop.getPicture());
        persistParserShop.setBaseUrl(parserShop.getBaseUrl());
        persistParserShop.setNeedLogin(parserShop.isNeedLogin());
        persistParserShop.setLogin(parserShop.getLogin());
        persistParserShop.setPassword(parserShop.getPassword());
        parserShopRepository.saveAndFlush(persistParserShop);
    }

    @Transactional
    public void removeParserShop(Long parserShopId) {
        parserShopRepository.deleteById(parserShopId);
    }

}
