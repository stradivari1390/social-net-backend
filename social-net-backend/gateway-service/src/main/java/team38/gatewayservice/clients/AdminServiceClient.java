package team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "admin-service", url = "http://109.184.122.25:8083")
public interface AdminServiceClient {

    @GetMapping("/admin-service-endpoint")
    ResponseEntity<String> adminEndpoint();
}