package kr.slack.integration.service.impl;

import kr.slack.integration.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service("stockService")
public class YahooFinancialService implements StockService {

    public Stock getStockInfo(String stockSymbol) {
        Stock stock = null;
        try {
            stock = YahooFinance.get(stockSymbol);
//            log.info("##### Name: " + stock.getName());
//            log.info("##### Symbol: " + stock.getSymbol());
//            log.info("##### StockExchange: " + stock.getStockExchange());
//            log.info("##### Quote: " + stock.getQuote().toString());
//            log.info("##### Current Quote: " + stock.getQuote().getPrice());
//            log.info("##### Currency: " + stock.getCurrency());

            stock.print();

            return stock;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BigDecimal getCurrentQuote(String stockSymbol) {
        return getStockInfo(stockSymbol).getQuote().getPrice();
    }

    @Override
    public Stock isQuoteLowerThan(String stockSymbol, BigDecimal targetQuote) {
        return null;
    }

    @Override
    public Stock isQuoteHigherThan(String stockSymbol, BigDecimal targetQuote) {
        return null;
    }
}
