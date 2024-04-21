package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.currency.exchange.basemaintenance.model.CurrencyCard;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.ShortCurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    @Mapping(target = "name", expression = "java(String.format(\"%d %s\",currency.getNominal(), currency.getName()))")
    @Mapping(target = "value", source = "value")
    ShortCurrencyDto convertToShortDto(Currency currency);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", expression = "java(Double.parseDouble(card.getValue().replace(\",\", \".\")))")
    @Mapping(target = "nominal", source = "nominal")
    @Mapping(target = "isoNumCode", source = "numCode")
    @Mapping(target = "isoCharCode", source = "charCode")
    Currency convertCardToEntity (CurrencyCard card);
}
