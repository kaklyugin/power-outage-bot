package org.roxy.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.roxy.crawler.dto.ParsingStatus;
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
        log.info("Started parsing page");
        List<PowerOutageParsedItem> items = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tbodyAll = doc.select("table.table_site1 tr:gt(1)");
        String lineNum = "0";
        for (Element row : tbodyAll) {
            try {
                lineNum = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(0).text().trim();
                String city = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(1).text().trim();
                String[] addresses = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(2).text().split(";");
                String powerOffDate = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(3).text().trim();
                String powerOnDate = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(4).text().trim();
                String powerOffTime = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(5).text().trim();
                String powerOnTime = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(6).text().trim();
                String reason = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(7).text().trim();
                String comment = row.getElementsByTag("tr").getFirst().getElementsByTag("td").get(8).text().trim();

                if (powerOffDate.isEmpty() || powerOnDate.isEmpty() || powerOffTime.isEmpty() || powerOnTime.isEmpty())
                {
                    log.warn("Power outage time interval is empty in row number =  {} for city = {} and will be skipped", lineNum, city);
                    continue;
                }

                for (String address : addresses) {
                    String trimmedAddress = address.trim();
                    try {
                        items.add(new PowerOutageParsedItem(
                                lineNum,
                                ParsingStatus.SUCCESS,
                                city,
                                trimmedAddress,
                                powerOffDate,
                                powerOnDate,
                                powerOffTime,
                                powerOnTime,
                                reason,
                                zoneId,
                                comment,
                                null));
                    }
                    catch (Exception e) {
                        log.error("Failed to create PowerOutageParsedItem from row = {}", row.html());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to parse row =  {} ", row);
                items.add(new PowerOutageParsedItem(lineNum,ParsingStatus.FAILED,row.html()));
            }
        }
        return items;
    }
}