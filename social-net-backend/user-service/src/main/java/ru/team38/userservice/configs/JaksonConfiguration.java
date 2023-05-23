package ru.team38.userservice.configs;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;


@Configuration
public class JaksonConfiguration {
  private static final String dateTimeZoneFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSz";

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer objectMapper() {
    return builder -> {
      builder.simpleDateFormat(dateTimeZoneFormat);
      builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeZoneFormat)));
    };
  }
}
