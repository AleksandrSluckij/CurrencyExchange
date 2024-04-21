package ru.skillbox.currency.exchange.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.currency.exchange.entity.Currency;


public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoNumCode(Long isoNumCode);

    List<Currency> findAllByIsoCharCodeIn(Set<String> strings);

}
