package kr.slack.integration.command.handler;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.webhook.WebhookResponse;
import kr.slack.integration.service.StockService;
import kr.slack.integration.service.impl.AlarmRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yahoofinance.Stock;

import java.io.IOException;
import java.math.BigDecimal;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;

@Slf4j
@Component
public class StockCommandHandler implements SlashCommandHandler {

    private final AlarmRequestService alarmRequestService;

    private final StockService stockService;

    public StockCommandHandler(AlarmRequestService alarmRequestService, StockService stockService) {
        this.alarmRequestService = alarmRequestService;
        this.stockService = stockService;
    }

    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {

        String text = slashCommandRequest.getPayload().getText();
        String[] args = text.split(" ");

        if (args.length == 1) {
            // assume that the command is to get the stock information
            responseStockInfo(args[0], slashCommandRequest, context);
        } else if (args.length == 2) {
            // assume that the command is to set the alarm with price
            createAlarmRequest(args[0], args[1], slashCommandRequest, context);
        } else {
            // error - to print the command usage
            context.ack("Usage examples: #1 /stock CRM, #2 /stock CRM 200");
        }
        return context.ack();
    }

    private void responseStockInfo(String stockSymbol, SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException {

        // TODO - check the stockSymbol is valid
        Stock stock = stockService.getStockInfo(stockSymbol);

        WebhookResponse result = context.respond(res -> res
                .responseType("in_channel")
                .blocks(asBlocks(
                        section(section -> section.text(markdownText(mt -> mt.text(
                                "The current quote of the stock, *" + stockSymbol + "*, is " + stock.getQuote().getPrice())))
                        ),
                        section(section -> section.text(markdownText(mt -> mt.text(
                                "For the stock detail, please click the following button.")))),
                        actions(actions -> actions.elements(asElements(
                                button(b -> b.text(plainText(pt -> pt.text("Detail on " + stockSymbol))).url("https://finance.yahoo.com/quote/" + stockSymbol))))
                        ),
                        divider()
                        )
                )
        );
    }

    private void createAlarmRequest(String stockSymbol, String target, SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {

        BigDecimal originalQuote = stockService.getCurrentQuote(stockSymbol);
        BigDecimal targetQuote = new BigDecimal(target);

        // TODO - validate the quotes to check if it is ok to create an alarm

        alarmRequestService.createAlarmRequest(context.getTeamId(), context.getChannelId(), context.getRequestUserId(), context.getResponseUrl(), stockSymbol, originalQuote, targetQuote);

        WebhookResponse result = context.respond(res -> res
                .responseType("in_channel")
                .blocks(asBlocks(
                                section(section -> section.text(markdownText(mt -> mt.text(
                                        "Thanks for setting the alarm for *" + stockSymbol + "*. " +
                                        "You will get notified when the quote gets " + target)))
                                ),
                                section(section -> section.text(markdownText(mt -> mt.text(
                                        "For the stock detail, please click the following button.")))),
                                actions(actions -> actions.elements(asElements(
                                        button(b -> b.text(plainText(pt -> pt.text("Detail on " + stockSymbol))).url("https://finance.yahoo.com/quote/" + stockSymbol)))
                                        )
                                ),
                                divider()
                        )
                )
        );
//        ChatPostMessageResponse response = context.say("You will be notified when the quote of " + stockSymbol + " gets " + target);
    }
}


