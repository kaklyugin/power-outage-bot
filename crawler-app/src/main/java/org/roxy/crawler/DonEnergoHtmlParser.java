package org.roxy.crawler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.roxy.crawler.dto.PowerOutageParsedItem;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class DonEnergoHtmlParser {

    private static final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public static List<PowerOutageParsedItem> parsePage(String html) {
        System.out.println("Started parsing " + Thread.currentThread().getName());
        List<PowerOutageParsedItem> items = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tbodyAll = doc.select("table.table_site1 tr:gt(1)");
        for (Element row : tbodyAll) {
            try {
                String id = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(0).text().trim();
                String city = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(1).text().trim();
                String[] addresses = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(2).text().split(";");
                String powerOffDate = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(3).text().trim();
                String powerOnDate = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(4).text().trim();
                String powerOffTime = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(5).text().trim();
                String powerOnTime = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(6).text().trim();
                String reason = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(7).text().trim();
                String comment = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(8).text().trim();

                if (powerOffDate.equals("") || powerOnDate.equals("") || powerOffTime.equals("") || powerOnTime.equals(""))
                {
                    log.warn("Power outage time interval is empty in row =  {} and will be skipped", row);
                    continue;
                }

                for (String address : addresses) {
                    String trimmedAddress = address.trim();
                    items.add(new PowerOutageParsedItem(
                            id,
                            city,
                            trimmedAddress,
                            powerOffDate,
                            powerOnDate,
                            powerOffTime,
                            powerOnTime,
                            reason,
                            zoneId,
                            comment));
                }
            } catch (Exception e) {
                log.error("Failed to parse row =  {} ", row);
                e.printStackTrace();
            }
        }
        return items;
    }

    private static List<String> getStreets(String items) {
        return List.of(items.split(";"));
    }
}
