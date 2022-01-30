package kr.slack.integration.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.model.event.AppHomeOpenedEvent;
import kr.slack.integration.action.MyBlockActionHandler;
import kr.slack.integration.command.handler.HelloCommandHandler;
import kr.slack.integration.command.handler.SearchCommandHandler;
import kr.slack.integration.event.handler.AppHomeOpenedEventHandler;
import kr.slack.integration.event.handler.DefaultMessageHandler;
import kr.slack.integration.middleware.ResponseDebugMiddleware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.regex.Pattern;

@Slf4j
@Configuration
public class SlackApp {

  @Autowired
  Environment env;

  @Autowired
  private SlashCommandHandler stockCommandHandler;

  @Bean
  public App initSlackApp() {

    final String ENV_SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    final String ENV_SLACK_APP_TOKEN = System.getenv("SLACK_APP_TOKEN");
    final String ENV_SLACK_SIGNING_SECRET = System.getenv("SLACK_SIGNING_SECRET");

    AppConfig appConfig = new AppConfig();
    appConfig.setSingleTeamBotToken(ENV_SLACK_BOT_TOKEN);
    appConfig.setSigningSecret(ENV_SLACK_SIGNING_SECRET);
    // Setting to false during development
    appConfig.setRequestVerificationEnabled(false);

    App app = new App(appConfig);

    // event handlers
    app.event(AppHomeOpenedEvent.class, new AppHomeOpenedEventHandler());

    // message event handlers
    app.message(Pattern.compile(".*"), new DefaultMessageHandler());

    // command handlers
    app.command("/hello", new HelloCommandHandler());

    app.command("/stock", stockCommandHandler);

    app.command("/my-search", new SearchCommandHandler());

    // action handlers
    app.blockAction("approve_button", new MyBlockActionHandler());

    // middleware
    if (env.getProperty("logging.level.com.slack.api").equalsIgnoreCase("DEBUG")) {
      app.use(new ResponseDebugMiddleware());
    }

    return app;
  }
}