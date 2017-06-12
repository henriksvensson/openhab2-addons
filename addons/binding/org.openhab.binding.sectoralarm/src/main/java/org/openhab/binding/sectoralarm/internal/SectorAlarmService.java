package org.openhab.binding.sectoralarm.internal;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectorAlarmService {

    // static String logOnPageUrl = "https://minasidor.sectoralarm.se/Users/Account/LogOn";
    // static String alarmSystemBaseUrl = "https://minasidor.sectoralarm.se/MyPages/Panel/AlarmSystem/";

    // For use with WireMock
    static String logOnPageUrl = "http://localhost:8080/Users/Account/LogOn";
    static String alarmSystemBaseUrl = "http://localhost:8080/MyPages/Panel/AlarmSystem/";

    static int timeout = 10000; // milliseconds

    private final Logger logger = LoggerFactory.getLogger(SectorAlarmService.class);

    public void main(String[] args) {

        // System.err.println("Usage: java -jar sectoralarmscraper.jar <user name> <password> <alarm system code>");

        String userName = args[0];
        String password = args[1];
        String alarmSystemCode = args[2];

        String alarmSystemUrl = alarmSystemBaseUrl + alarmSystemCode + "?ethernetStatus=online&locksAvailable=False";

        try {
            Response r = Jsoup.connect(logOnPageUrl).timeout(timeout).execute();

            Response response = Jsoup.connect(logOnPageUrl + "?Returnurl=~%2F").data("userNameOrEmail", userName)
                    .data("password", password).cookies(r.cookies()).method(Method.POST).timeout(timeout).execute();

            Document doc = Jsoup.connect(alarmSystemUrl).cookies(response.cookies()).timeout(timeout).get();

            Element status = doc.getElementsByClass("panel-status-title").first();
            Element time = doc.getElementsByClass("panel-status-time").first(); // Also contains info on WHO
                                                                                // armed/disarmed the alarm. For example
                                                                                // "6/11 20:02 (av 4)".

            logger.debug(status.html());
            logger.debug(time.html());

        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

    }

}
