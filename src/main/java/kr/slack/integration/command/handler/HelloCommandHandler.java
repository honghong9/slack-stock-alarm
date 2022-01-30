package kr.slack.integration.command.handler;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

public class HelloCommandHandler implements SlashCommandHandler {

    @Override
    public Response apply(SlashCommandRequest slashCommandRequest, SlashCommandContext context) throws IOException {
        WebhookResponse result = context.respond(res -> res
                .responseType("ephemeral")
                .text("Hi there!")
        );
        return context.ack();
    }
}
