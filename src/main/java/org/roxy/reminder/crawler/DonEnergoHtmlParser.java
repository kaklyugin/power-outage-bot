package org.roxy.reminder.crawler;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.roxy.reminder.crawler.dto.PowerOutageItem;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class DonEnergoHtmlParser {

    private static final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public static List<PowerOutageItem> parsePage(String html) {
        System.out.println("started parsing " + Thread.currentThread().getName());
        List<PowerOutageItem> items = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tbodyAll = doc.select("table.table_site1 tr:gt(1)");
        tbodyAll.forEach(row -> {
            items.add(new PowerOutageItem(
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(0).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(1).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(2).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(3).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(4).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(5).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(6).text(),
                    row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(7).text(),
                    zoneId
            ));
        });
        return items;
    }

    private static List<String> getStreets (String items) {
        return List.of(items.split(";"));
    }

}
