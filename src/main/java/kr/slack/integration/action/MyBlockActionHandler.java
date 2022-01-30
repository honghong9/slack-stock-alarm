package kr.slack.integration.action;

import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.handler.builtin.BlockActionHandler;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;

public class MyBlockActionHandler implements BlockActionHandler {

    @Override
    public Response apply(BlockActionRequest blockActionRequest, ActionContext context) throws IOException, SlackApiException {
            String value = blockActionRequest.getPayload().getActions().get(0).getValue(); // "button's value"
            // Do something in response
            return context.ack();
    }
}
