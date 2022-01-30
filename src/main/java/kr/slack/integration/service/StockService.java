package kr.slack.integration.service;

import yahoofinance.Stock;

import java.math.BigDecimal;

public interface StockService {

    Stock getStockInfo(String stockSymbol);

    BigDecimal getCurrentQuote(String stockSymbol);

}