package ru.kazov.collectivepurchases.server.common;

import ru.kazov.collectivepurchases.server.models.dao.User;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserCategory;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserItem;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;
import ru.kazov.collectivepurchases.server.models.dao.sale.Sale;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleItem;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserCategoryDTO;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserItemDTO;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserJobDTO;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserShopDTO;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleCategoryDTO;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleDTO;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleItemDTO;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleOwnerDTO;

import java.util.List;

public class ObjectMapper {

    public SaleOwnerDTO convertSaleOwner(User src){
        SaleOwnerDTO dest = new SaleOwnerDTO();
        dest.setId(src.getId());
        dest.setFullname(src.getFullname());
        dest.setEmail(src.getEmail());
        dest.setVkId(src.getVkId());
        dest.setTelegramId(src.getTelegramId());
        return dest;
    }

    public User convertSaleOwner(SaleOwnerDTO src){
        User dest = new User();
        dest.setId(src.getId());
        dest.setFullname(src.getFullname());
        dest.setEmail(src.getEmail());
        dest.setVkId(src.getVkId());
        dest.setTelegramId(src.getTelegramId());
        return dest;
    }


    public SaleDTO convertSale(Sale src){
        SaleDTO dest = new SaleDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setActive(src.isActive());
        dest.setPicture(src.getPicture());
        dest.setCountry(src.getCountry());
        dest.setStartDate(src.getStartDate());
        dest.setEndDate(src.getEndDate());
        if(src.getOwner()!=null)
            dest.setOwnerId(src.getOwner().getId());
        return dest;
    }

    public Sale convertSale(SaleDTO src){
        Sale dest = new Sale();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setActive(src.isActive());
        dest.setPicture(src.getPicture());
        dest.setCountry(src.getCountry());
        dest.setStartDate(src.getStartDate());
        dest.setEndDate(src.getEndDate());

        dest.setOwner(new User());
        dest.getOwner().setId(src.getOwnerId());
        return dest;
    }

    public List<SaleDTO> convertSales(List<Sale> srcList){
        return srcList.stream().map(this::convertSale).toList();
    }



    public SaleCategoryDTO convertSaleCategory(SaleCategory src){
        SaleCategoryDTO dest = new SaleCategoryDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        if(src.getSale()!=null)
            dest.setSaleId(src.getSale().getId());
        return dest;
    }

    public SaleCategory convertSaleCategory(SaleCategoryDTO src){
        SaleCategory dest = new SaleCategory();
        dest.setId(src.getId());
        dest.setName(src.getName());

        dest.setSale(new Sale());
        dest.getSale().setId(src.getSaleId());
        return dest;
    }

    public List<SaleCategoryDTO> convertSaleCategories(List<SaleCategory> srcList){
        return srcList.stream().map(this::convertSaleCategory).toList();
    }



    public SaleItemDTO convertSaleItem(SaleItem src){
        SaleItemDTO dest = new SaleItemDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDescription(src.getDescription());
        dest.setPriceComment(src.getPriceComment());
        dest.setUrl(src.getUrl());
        dest.setVkPhotoUrl(src.getVkPhotoUrl());
        dest.setPrices(src.getPrices());
        dest.setPictures(src.getPictures());
        dest.setProperties(src.getProperties());
        return dest;
    }

    public SaleItem convertSaleItem(SaleItemDTO src){
        SaleItem dest = new SaleItem();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDescription(src.getDescription());
        dest.setPriceComment(src.getPriceComment());
        dest.setUrl(src.getUrl());
        dest.setVkPhotoUrl(src.getVkPhotoUrl());
        dest.setPrices(src.getPrices());
        dest.setPictures(src.getPictures());
        dest.setProperties(src.getProperties());
        return dest;
    }

    public List<SaleItemDTO> convertSaleItems(List<SaleItem> srcList){
        return srcList.stream().map(this::convertSaleItem).toList();
    }




    public ParserCategoryDTO convertParserCategory(ParserCategory src){
        ParserCategoryDTO dest = new ParserCategoryDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDate(src.getDate());
        return dest;
    }

    public ParserCategory convertParserCategory(ParserCategoryDTO src){
        ParserCategory dest = new ParserCategory();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDate(src.getDate());
        return dest;
    }

    public List<ParserCategoryDTO> convertParserCategories(List<ParserCategory> srcList){
        return srcList.stream().map(this::convertParserCategory).toList();
    }



    public ParserItemDTO convertParserItem(ParserItem src){
        ParserItemDTO dest = new ParserItemDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDescription(src.getDescription());
        dest.setUrl(src.getUrl());
        dest.setPrices(src.getPrices());
        dest.setPictures(src.getPictures());
        dest.setProperties(src.getProperties());
        if(src.getCategory()!=null)
            dest.setCategoryId(src.getCategory().getId());
        return dest;
    }

    public ParserItem convertParserItem(ParserItemDTO src){
        ParserItem dest = new ParserItem();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDescription(src.getDescription());
        dest.setUrl(src.getUrl());
        dest.setPrices(src.getPrices());
        dest.setPictures(src.getPictures());
        dest.setProperties(src.getProperties());

        dest.setCategory(new ParserCategory());
        dest.getCategory().setId(src.getCategoryId());
        return dest;
    }

    public List<ParserItemDTO> convertParserItems(List<ParserItem> srcList){
        return srcList.stream().map(this::convertParserItem).toList();
    }

    public ParserJobDTO convertParserJob(ParserJob src){
        ParserJobDTO dest = new ParserJobDTO();
        dest.setId(src.getId());
        dest.setStatus(src.getStatus());
        dest.setMessage(src.getMessage());
        dest.setUrl(src.getUrl());
        if(src.getShop()!=null)
            dest.setParserShopId(src.getShop().getId());
        return dest;
    }

    public ParserJob convertParserJob(ParserJobDTO src){
        ParserJob dest = new ParserJob();
        dest.setId(src.getId());
        dest.setStatus(src.getStatus());
        dest.setMessage(src.getMessage());
        dest.setUrl(src.getUrl());
        dest.setShop(new ParserShop());
        dest.getShop().setId(src.getParserShopId());
        return dest;
    }

    public List<ParserJobDTO> convertParserJobs(List<ParserJob> srcList){
        return srcList.stream().map(this::convertParserJob).toList();
    }

    public ParserShopDTO convertParserShop(ParserShop src){
        ParserShopDTO dest = new ParserShopDTO();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setBaseUrl(src.getBaseUrl());
        dest.setPicture(src.getPicture());
        dest.setLogin(src.getLogin());
        dest.setPassword(src.getPassword());
        dest.setNeedLogin(src.isNeedLogin());
        return dest;
    }

    public ParserShop convertParserShop(ParserShopDTO src){
        ParserShop dest = new ParserShop();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setBaseUrl(src.getBaseUrl());
        dest.setPicture(src.getPicture());
        dest.setLogin(src.getLogin());
        dest.setPassword(src.getPassword());
        dest.setNeedLogin(src.isNeedLogin());
        return dest;
    }

    public List<ParserShopDTO> convertParserShops(List<ParserShop> srcList){
        return srcList.stream().map(this::convertParserShop).toList();
    }

    public SaleItem convertParserItemToSaleItem(ParserItem src){
        SaleItem dest = new SaleItem();
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setDescription(src.getDescription());
        dest.setUrl(src.getUrl());
        dest.setPrices(src.getPrices());
        dest.setPictures(src.getPictures());
        dest.setProperties(src.getProperties());
        return dest;
    }
}
