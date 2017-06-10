package org.openhab.binding.sectoralarm.internal;

import java.io.IOException;
import java.sql.Connection;

import org.jsoup.Jsoup;

public class SectorAlarmService {

    // static String logOnPageUrl = "https://minasidor.sectoralarm.se/Users/Account/LogOn";
    // static String alarmSystemBaseUrl = "https://minasidor.sectoralarm.se/MyPages/Panel/AlarmSystem/";

    // For use with WireMock
    static String logOnPageUrl = "http://localhost:8080/Users/Account/LogOn";
    static String alarmSystemBaseUrl = "http://localhost:8080/MyPages/Panel/AlarmSystem/";

    static int timeout = 10000; // milliseconds

    public static void main(String[] args) {

        // Expect user name, password and alarm system code as program arguments.
        if (args.length != 3) {
            System.err.println("Usage: java -jar sectoralarmscraper.jar <user name> <password> <alarm system code>");
            return;
        }

        String userName = args[0];
        String password = args[1];
        String alarmSystemCode = args[2];

        String alarmSystemUrl = alarmSystemBaseUrl + alarmSystemCode + "?ethernetStatus=online&locksAvailable=False";

        try {
            Connection.Response r = Jsoup.connect(logOnPageUrl).timeout(timeout).execute();

            Connection.Response response = Jsoup.connect(logOnPageUrl + "?Returnurl=~%2F")
                    .data("userNameOrEmail", userName).data("password", password).cookies(r.cookies())
                    .method(Connection.Method.POST).timeout(timeout).execute();

            Document doc = Jsoup.connect(alarmSystemUrl).cookies(response.cookies()).timeout(timeout).get();

            Element status = doc.getElementsByClass("panel-status-title").first();
            Element time = doc.getElementsByClass("panel-status-time").first(); // Also contains info on WHO
                                                                                // armed/disarmed the alarm. For example
                                                                                // "6/11 20:02 (av 4)".

            System.out.println(status.html());
            System.out.println(time.html());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
