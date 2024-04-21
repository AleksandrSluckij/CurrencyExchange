package ru.skillbox.currency.exchange.basemaintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyCard {
    @XmlElement(name = "NumCode")
    private Long numCode;
    @XmlElement(name = "CharCode")
    private String charCode;
    @XmlElement(name = "Nominal")
    private Long nominal;
    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "Value")
    private String value;
}
