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
public class YahooFinancialStockService implements StockService {

    public Stock getStockInfo(String stockSymbol) {
        try {
            return YahooFinance.get(stockSymbol);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error in getting stock info from Yahoo Finance");
        }
        return null;
    }

    @Override
    public BigDecimal getCurrentQuote(String stockSymbol) {
        return getStockInfo(stockSymbol).getQuote().getPrice();
    }
}
