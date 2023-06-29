package ru.team38.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.team38.common.aspects.LoggerAspect;

@SpringBootApplication
@Import({LoggerAspect.class})
public class UserService {
    public static void main(String[] args) {
        SpringApplication.run(UserService.class);
    }
}
