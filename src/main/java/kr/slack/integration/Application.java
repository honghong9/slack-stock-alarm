package kr.slack.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan(basePackages = "kr.slack.integration")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}