
package kr.slack.integration.scheduler;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import kr.slack.integration.domain.AlarmRequest;
import kr.slack.integration.service.StockService;
import kr.slack.integration.service.impl.AlarmRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Component
public class DefaultStockCheckScheduler {

   private String ENV_SLACK_BOT_TOKEN;

   @Autowired
   private Environment env;

   private final StockService stockService;
   private final AlarmRequestService alarmRequestService;

   public DefaultStockCheckScheduler(StockService stockService, AlarmRequestService alarmRequestService) {
      this.stockService = stockService;
      this.alarmRequestService = alarmRequestService;
      ENV_SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
   }

   @Scheduled(fixedDelay = 10000, initialDelay = 10000)
   public void checkQuoteAndNotify() {

      log.info("##### checkQuoteAndNotify() in");
      // fetch all the not processed alarm requests
      try {
         process();
      } catch (SlackApiException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void process() throws SlackApiException, IOException {

      for (AlarmRequest request : alarmRequestService.getAllNotProcessedAlarmRequests()) {

         int compareResult =  request.getOriginalQuote().compareTo(request.getTargetQuote());
         if (compareResult == 0) {
            alarmRequestService.updateProcessed(request.getId(), Boolean.TRUE);
            continue;
         }

         BigDecimal currentQuote = stockService.getCurrentQuote(request.getStockSymbol());
         int compareMarketResult =  request.getTargetQuote().compareTo(currentQuote);

         // targetQuote > currentQuote
         if (compareResult == 1)  {
            // originalQuote > targetQuote
            if (compareMarketResult == 1 ) {
               // originalQuote > targetQuote > currentQuote
               responseMessage(request, currentQuote);
               alarmRequestService.updateProcessed(request.getId(), Boolean.TRUE);
            }
         }

         // originalQuote < targetQuote
         if (compareResult == -1) {
            // targetQuote < currentQuote
            if (compareMarketResult == -1 ) {
               // originalQuote < targetQuote < currentQuote
               responseMessage(request, currentQuote);
               alarmRequestService.updateProcessed(request.getId(), Boolean.TRUE);
            }
         }
      }
   }

   private void responseMessage(AlarmRequest request, BigDecimal currentQuote) throws SlackApiException, IOException {
      Slack slack = Slack.getInstance();
      ChatPostMessageResponse response = slack.methods(ENV_SLACK_BOT_TOKEN).chatPostMessage(
              req -> req.channel(request.getChannelId()).text("<@"+ request.getUserId() + "> "
              + "originalQuote: " + request.getOriginalQuote()
              + ", targetQuote: " + request.getTargetQuote()
              + ", currentQuote: " + currentQuote
      ));
   }
}