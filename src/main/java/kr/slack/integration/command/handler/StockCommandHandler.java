package kr.slack.integration.command.handler;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.WebhookResponse;
import kr.slack.integration.service.StockService;
import kr.slack.integration.service.impl.AlarmRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yahoofinance.Stock;

import java.io.IOException;
import java.math.BigDecimal;

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
            returnStockInfo(args[0], slashCommandRequest, context);
        } else if (args.length == 2) {
            // assume that the command is to set the alarm with price
            createAlarmRequest(args[0], args[1], slashCommandRequest, context);
        } else {
            // error - to print the command usage
            context.ack("Usage examples: #1 /stock CRM, #2 /stock CRM 200");
        }
        return context.ack();
    }

    private void returnStockInfo(String stockSymbol, SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException {

        // TODO - check the stockSymbol is valid
        Stock stock = stockService.getStockInfo(stockSymbol);

        WebhookResponse result = context.respond(res -> res
                .responseType("in_channel")
                .text(stock.toString())
        );
    }

    private void createAlarmRequest(String stockSymbol, String target, SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException, SlackApiException {

        BigDecimal originalQuote = stockService.getCurrentQuote(stockSymbol);
        BigDecimal targetQuote = new BigDecimal(target);

        alarmRequestService.createAlarmRequest(context.getTeamId(), context.getChannelId(), context.getRequestUserId(), context.getResponseUrl(), stockSymbol, originalQuote, targetQuote);

//        WebhookResponse result = context.respond(res -> res
//                .responseType("in_channel")
//                .text("You will be notified when the quote of " + stockSymbol + " gets " + target)
//        );

        ChatPostMessageResponse response = context.say("You will be notified when the quote of " + stockSymbol + " gets " + target);
    }

    private void temp() {
//        app.command("/hello2", (req, ctx) -> {
//            // ctx.client() holds a valid bot token
//            ChatPostMessageResponse response = ctx.client().chatPostMessage(r -> r
//                    .channel(ctx.getChannelId())
//                    .text(":wave: How are you?")
//            );
//            return ctx.ack();
//        });
//
//        app.command("/hello3", (req, ctx) -> {
//            ChatPostMessageResponse response = ctx.say(":wave: How are you?");
//            return ctx.ack();
//        });
//
//        app.command("/weather", (req, ctx) -> {
//            String keyword = req.getPayload().getText();
//            String userId = req.getPayload().getUserId();
//            ctx.logger.info("Weather search by keyword: {} for user: {}", keyword, userId);
//            return ctx.ack("Weather search by keyword: " + keyword + " for user: " + userId);
//        });

//
//        app.command("/ping2", (req, ctx) -> {
//            return ctx.ack(asBlocks(
//                    section(section -> section.text(markdownText(":wave: pong"))),
//                    actions(actions -> actions
//                            .elements(asElements(
//                                    button(b -> b.actionId("ping-again").text(plainText(pt -> pt.text("Ping"))).value("ping"))
//                            ))
//                    )
//            ));
//        });
//
//        app.command("/ping3", (req, ctx) -> {
//            return ctx.ack(res -> res.responseType("in_channel").text(":wave: pong"));
//        });
    }
}


