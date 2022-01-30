package kr.slack.integration.command.handler;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.search.SearchMessagesResponse;

import java.io.IOException;

public class SearchCommandHandler implements SlashCommandHandler {

    @Override
    public Response apply(SlashCommandRequest request, SlashCommandContext context) throws IOException, SlackApiException {
        String query = request.getPayload().getText();
        if (query == null || query.trim().length() == 0) {
            return context.ack("Please give some query.");
        }
        String userToken = context.getRequestUserToken(); // enabling InstallationService required
        if (userToken != null) {
            SearchMessagesResponse response = context.client().searchMessages(r -> r
                    .token(userToken) // Overwrite underlying bot token with the given user token
                    .query(query));
            if (response.isOk()) {
                String reply = response.getMessages().getTotal() + " results found for " + query;
                return context.ack(reply);
            } else {
                String reply = "Failed to search by " + query + " (error: " + response.getError() + ")";
                return context.ack(reply);
            }
        } else {
            return context.ack("Please allow this Slack app to run search queries for you.");
        }
    }
}


