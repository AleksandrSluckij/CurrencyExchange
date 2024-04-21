package ru.skillbox.currency.exchange.basemaintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CbrCurrencyMarketInfo {
    @XmlElement(name = "Valute")
    private List<CurrencyCard> cards;
}
