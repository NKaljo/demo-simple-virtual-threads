package dev.nkaljo.vt_blocker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockController.class);

    @GetMapping("/block/{seconds}")
    public void block(@PathVariable Long seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
        LOGGER.info("Blocked for {} seconds", seconds);
    }
}
