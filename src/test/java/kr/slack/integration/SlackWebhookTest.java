package kr.slack.integration;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Slf4j
@SpringBootTest
public class SlackWebhookTest {

    @Autowired
    Environment env;

    @Before
    public void setup() {
    }

    @Test
    public void sendMessage() throws IOException, SlackApiException {

        Slack slack = Slack.getInstance();

        ChatPostMessageResponse response = slack.methods(env.getProperty("SLACK_BOT_TOKEN"))
                .chatPostMessage(req -> req.channel("C02T10A97L6").text("<@U02TL9UKJ01> TEST"));

        log.info(response.toString());
    }
}