package kr.slack.integration.event.handler;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.MessageEvent;
import org.slf4j.Logger;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.regex.Pattern;

public class DefaultMessageHandler implements BoltEventHandler<MessageEvent> {

    private final Pattern stockPattern = Pattern.compile("^.*" + Pattern.quote("stock") + ".*$");
    private final Pattern crmPattern = Pattern.compile("^.*" + Pattern.quote("crm") + ".*$");

    @Override
    public Response apply(EventsApiPayload<MessageEvent> payload, EventContext context) throws IOException, SlackApiException {

        Logger logger = context.logger;
        try {
            MessageEvent event = payload.getEvent();
            String text = event.getText();

            if (ObjectUtils.isEmpty(text)) {
                return context.ack();
            }

            // all messages come here, and do something according to the patterns defined here
            // TODO - find the more efficient way to improve performance when the number of patterns is large.
            logger.info("##### message text: {}", text);

            // handle the text including hello pattern
            if (stockPattern.matcher(text).matches()) {

                // Call the chat.postMessage method using the built-in WebClient
                ChatPostMessageResponse result = context.client().chatPostMessage(r -> r
                        // The token you used to initialize your app is stored in the `context` object
                        .token(context.getBotToken())
                        // Payload message should be posted in the channel where original message was heard
                        .channel(event.getChannel())
                        .text("world")
                );

                logger.info("##### result: {}", result);
            }

            // handle the text including CRM pattern
            if (crmPattern.matcher(text).matches()) {

                // Call the chat.postMessage method using the built-in WebClient
                ChatPostMessageResponse result = context.client().chatPostMessage(r -> r
                        // The token you used to initialize your app is stored in the `context` object
                        .token(context.getBotToken())
                        // Payload message should be posted in the channel where original message was heard
                        .channel(event.getChannel())
                        .text("Would you want to check Salesforce' stock?")
                );

                logger.info("##### result: {}", result);
            }

        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return context.ack();
    }
}