package kr.slack.integration.event.handler;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;

import java.io.IOException;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.view.Views.view;

public class AppHomeOpenedEventHandler implements BoltEventHandler<AppHomeOpenedEvent> {

    @Override
    public Response apply(EventsApiPayload<AppHomeOpenedEvent> payload, EventContext context) throws IOException, SlackApiException {

        View appHomeView = view(view -> view
                .type("home")
                .blocks(asBlocks(
                        section(section -> section.text(markdownText(mt -> mt.text("*Welcome to StockAlarm* :tada:")))),
                        divider(),
                        section(section -> section.text(markdownText(mt -> mt.text("You can be notified when a stock price gets a certain level."))))
                        )
                )
        );

        ViewsPublishResponse viewsPublishResponse = context.client().viewsPublish(r -> r
                .userId(payload.getEvent().getUser())
                .view(appHomeView)
        );

        return context.ack();
    }
}
