package org.roxy;

import org.junit.jupiter.api.Test;
import org.roxy.crawler.DonEnergoHtmlParser;
import org.roxy.crawler.dto.PowerOutageParsedItem;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestParser {

    @Test
    void testParsePage55() throws IOException, IOException {
        ClassPathResource resource = new ClassPathResource("url_55.html");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        List<PowerOutageParsedItem> result = DonEnergoHtmlParser.parsePage(content);
        for (PowerOutageParsedItem item : result) {
            System.out.println(item);
        }
    }
}

