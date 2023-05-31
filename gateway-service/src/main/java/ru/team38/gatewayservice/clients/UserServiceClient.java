package ru.team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.team38.userservice.security.dto.UserDto;


@FeignClient(name = "user-service", url = "http://109.184.122.25:8081")
public interface UserServiceClient {

    @GetMapping("/user-service-endpoint")
    ResponseEntity<String> userEndpoint();

    @GetMapping("/user-details-by-username-endpoint")
    UserDetails getUserDetailsByUsername(@RequestParam("username") String username);

    @GetMapping("/user-details-by-email-endpoint")
    UserDetails getUserDetailsByEmail(@RequestParam("email") String email);

    @PostMapping("/user-registration-endpoint")
    void sendUserDtoDataForRegistration(@RequestBody UserDto userDto);
}