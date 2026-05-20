package org.crm.salesmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "org.crm.salesmanager")
@EnableScheduling
public class SalesManagerApp {

  public static void main(String[] args) {
    SpringApplication.run(SalesManagerApp.class, args);
  }
}
