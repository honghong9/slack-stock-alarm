package kr.slack.integration.middleware;

import com.slack.api.bolt.middleware.Middleware;
import com.slack.api.bolt.middleware.MiddlewareChain;
import com.slack.api.bolt.request.Request;
import com.slack.api.bolt.response.Response;
import com.slack.api.bolt.util.JsonOps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class ResponseDebugMiddleware implements Middleware {

    @Override
    public Response apply(Request request, Response _response, MiddlewareChain chain) throws Exception {

        Response response = chain.next(request);

        if (response.getStatusCode() != 200) {
            response.getHeaders().put("content-type", Arrays.asList(response.getContentType()));
            // dump all the headers as a single string
            String headers = response.getHeaders().entrySet().stream()
                    .map(e -> e.getKey() +  ": " + e.getValue() + "\n").collect(joining());

            String responseText =
                    ":warning: *[DEBUG MODE] Something is technically wrong* :warning:\n" +
                            "Below is a response the Slack app was going to send...\n" +
                            "*Status Code*: " + response.getStatusCode() + "\n" +
                            "*Headers*: ```" + headers + "```" + "\n" +
                            "*Body*: ```" + response.getBody() + "```";

            // set an ephemeral message with useful information
            Map bodyMap = new HashMap();
            bodyMap.put("responseType", "ephemeral");
            bodyMap.put("text", responseText);

            response.setBody(JsonOps.toJsonString(bodyMap));

            response.setStatusCode(200);
        }
        return response;
    }
}