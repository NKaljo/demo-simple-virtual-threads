package dev.nkaljo.vt_main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final RestClient restClient;

    public HomeController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8085")
                .build();
    }

    @GetMapping("/block/{seconds}")
    public String home(@PathVariable Long seconds) {
        final var response = restClient.get()
                .uri("/block/{seconds}", seconds)
                .retrieve()
                .toBodilessEntity();

        LOGGER.info("{} on {}", response.getStatusCode(), Thread.currentThread());

        return Thread.currentThread().toString();
    }
}
