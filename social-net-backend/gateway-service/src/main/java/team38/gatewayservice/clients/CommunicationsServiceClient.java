package team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "communications-service", url = "http://109.184.122.25:8082")
public interface CommunicationsServiceClient {

    @GetMapping("/communications-service-endpoint")
    ResponseEntity<String> communicationsEndpoint();
}