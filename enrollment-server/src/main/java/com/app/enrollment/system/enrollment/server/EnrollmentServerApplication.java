package com.app.enrollment.system.enrollment.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnrollmentServerApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(EnrollmentServerApplication.class, args);
  }
  
}
