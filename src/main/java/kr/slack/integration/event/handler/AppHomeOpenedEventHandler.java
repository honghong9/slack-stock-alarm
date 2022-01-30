package kr.slack.integration.event.handler;

import com.slack.api.app_backend.events.payload.AppHomeOpenedPayload;
import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.model.view.Views.view;

public class AppHomeOpenedEventHandler implements BoltEventHandler<AppHomeOpenedEvent> {

    @Override
    public Response apply(EventsApiPayload<AppHomeOpenedEvent> payload, EventContext context) throws IOException, SlackApiException {

        View appHomeView = view(view -> view
                .type("home")
                .blocks(asBlocks(
                        section(section -> section.text(markdownText(mt -> mt.text("*Welcome to MyStockTracking* :tada:")))),
                        divider(),
                        section(section -> section.text(markdownText(mt -> mt.text("This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                        actions(actions -> actions
                                .elements(asElements(
                                        button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1").actionId("button_1"))
                                ))
                        )
                ))
        );

        ViewsPublishResponse viewsPublishResponse = context.client().viewsPublish(r -> r
                .userId(payload.getEvent().getUser())
                .view(appHomeView)
        );

        return context.ack();
    }
}
