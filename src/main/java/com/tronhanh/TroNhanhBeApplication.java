package com.tronhanh;

import com.tronhanh.constant.AppConstant;
import com.tronhanh.util.DotenvUtils;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Tro Nhanh backend application.
 * Automatically sets JVM default timezone to UTC and loads local .env files if present.
 */
@SpringBootApplication
public class TroNhanhBeApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone(AppConstant.DEFAULT_TIMEZONE));
    DotenvUtils.loadEnvFile();
    SpringApplication.run(TroNhanhBeApplication.class, args);
  }
}
