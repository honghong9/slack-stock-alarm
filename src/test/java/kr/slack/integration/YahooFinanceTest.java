package kr.slack.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class YahooFinanceTest {

    private final String STOCK_SYMBOL_CRM = "CRM";

    @Before
    public void setup() {
    }

    @After
    public void cleanup() {
    }

    @Test
    public void test_get_stock() throws IOException {

        Stock stock = YahooFinance.get(STOCK_SYMBOL_CRM);

        assertThat(stock.getSymbol()).isEqualTo(STOCK_SYMBOL_CRM);
        assertThat(stock.getName()).isEqualTo("salesforce.com, inc.");

        log.info("##### Name: " + stock.getName());
        log.info("##### Symbol: " + stock.getSymbol());
        log.info("##### StockExchange: " + stock.getStockExchange());
        log.info("##### Quote: " + stock.getQuote().toString());
        log.info("##### Current Quote: " + stock.getQuote().getPrice());
        log.info("##### Currency: " + stock.getCurrency());

        stock.print();
    }

    @Test
    public void test_get_historical_quotes() throws IOException {

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.DATE, -3);

        Stock stock = YahooFinance.get(STOCK_SYMBOL_CRM, from, to, Interval.DAILY);

        assertThat(stock.getSymbol()).isEqualTo(STOCK_SYMBOL_CRM);
        assertThat(stock.getName()).isEqualTo("salesforce.com, inc.");
        assertThat(stock.getHistory()).isNotEmpty();

        List<HistoricalQuote> historicalQuotes = stock.getHistory();
        for(HistoricalQuote historicalQuote : historicalQuotes) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(historicalQuote.getDate().toInstant(), ZoneId.systemDefault());
            log.info("##### Closing Quote as of " + dateTime.toLocalDate() + ": " + historicalQuote.getClose().toString());
        }

        stock.print();
    }
}