package ru.skillbox.currency.exchange.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
@Service
public class ParsePageHTML {
    private final CurrencyRepository currencyRepository;

    private final String url;
    private final String user="Mozilla/5.0 (Windows; U; WindowsNT 5.1;" +
            " en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String referrer="http://www.google.com";

    public ParsePageHTML(CurrencyRepository currencyRepository,
                         @Value("${binance.api.getPrice}")String url) {
        this.currencyRepository = currencyRepository;
        this.url=url;
    }
    @Scheduled(fixedDelay = 360000)
    public void parsePage() {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(user).referrer(referrer).get();
            Elements links = doc.select("Valute");

            for (Element element : links) {
                Elements numCode = element.select("NumCode");
                Long isoNumCode= Long.valueOf(numCode.text());
                if (currencyRepository.existsByIsoNumCode(isoNumCode)) {
                    Currency currency = currencyRepository.findByIsoNumCode(isoNumCode);
                    currency.setIsoCharCode(element.select("CharCode").text());
                    String str=element.select("Value").text().replaceAll(",",".");
                    currency.setValue(Double.valueOf(str));
                    currencyRepository.save(currency);
                }else {
                    Currency currency= new Currency();
                    currency.setName(element.select("Name").text());
                    currency.setNominal(Long.valueOf(element.select("Nominal").text()));
                    String str=element.select("Value").text().replaceAll(",",".");
                    currency.setValue(Double.valueOf(str));
                    currency.setIsoNumCode(Long.valueOf(element.select("NumCode").text()));
                    currency.setIsoCharCode(element.select("CharCode").text());
                    currencyRepository.save(currency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
