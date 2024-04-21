package ru.skillbox.currency.exchange.basemaintenance;

import java.net.URI;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skillbox.currency.exchange.basemaintenance.model.CurrencyCard;
import ru.skillbox.currency.exchange.basemaintenance.model.CbrCurrencyMarketInfo;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrenciesBaseUpdating {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper mapper;

    @Value("${cbr_currency_market_url}")
    String cbrUrl;


    @Scheduled(fixedDelay = 240000, initialDelay = 5000)
    public void updateInfoCbr () {
        log.info("---Retrieving info from CBR site!---");
        URL cbrInfoUrl = getUrl(cbrUrl);
        if (cbrInfoUrl == null) {
            return;
        }

        CbrCurrencyMarketInfo marketInfo = getCbrCurrencyMarketInfo(cbrInfoUrl);
        if (marketInfo == null) {
            return;
        }

        List<CurrencyCard> allCurrencyCards = marketInfo.getCards();
        log.info("Info from CBR site retrieved. Total {} currency cards received.", allCurrencyCards.size());

        Map<String,CurrencyCard> allCurrenciesMap = allCurrencyCards.stream()
            .collect(Collectors.toMap(CurrencyCard::getCharCode, currencyCard -> currencyCard));
        List<Currency> currenciesToUpdate = currencyRepository.findAllByIsoCharCodeIn(allCurrenciesMap.keySet());
        if (!currenciesToUpdate.isEmpty()) {
            updateRecords(currenciesToUpdate, allCurrenciesMap);
        }
        if (!allCurrenciesMap.isEmpty()) {
            addRecords(allCurrenciesMap);
        }

        log.info("Database updated.");
    }

    private void addRecords(Map<String, CurrencyCard> allCurrenciesMap) {
        List<Currency> currenciesToAdd = allCurrenciesMap.values().stream()
            .map(mapper::convertCardToEntity)
            .collect(Collectors.toList());
        currencyRepository.saveAllAndFlush(currenciesToAdd);
        log.info("{} records added.", currenciesToAdd.size());
    }

    private void updateRecords(List<Currency> currenciesToUpdate,
        Map<String, CurrencyCard> allCurrenciesMap) {
        for (Currency currency : currenciesToUpdate) {
            String key = currency.getIsoCharCode();
            Currency newValues = mapper.convertCardToEntity(allCurrenciesMap.get(key));
            currency.setName(newValues.getName());
            currency.setValue(newValues.getValue());
            currency.setNominal(newValues.getNominal());
            currency.setIsoNumCode(newValues.getIsoNumCode());
            allCurrenciesMap.remove(key);
        }
        currencyRepository.saveAllAndFlush(currenciesToUpdate);
        log.info("{} records updated.", currenciesToUpdate.size());
    }

    private CbrCurrencyMarketInfo getCbrCurrencyMarketInfo(URL cbrInfoUrl) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CbrCurrencyMarketInfo.class);
            return (CbrCurrencyMarketInfo) jaxbContext.createUnmarshaller().unmarshal(cbrInfoUrl);
        } catch (JAXBException e) {
            log.error("Info from CBR site not retrieved! Check data structure or internet connection. Database not updated!");
            return null;
        }
    }

    private URL getUrl(String urlString) {
        try {
            return new URI(urlString).toURL();
        } catch (Exception ex) {
            log.error("An address '{}' of CBR info not recognized. Check configuration property.", cbrUrl);
            return null;
        }
    }
}
