/*
  Copyright (c) 2010-2017 by the respective copyright holders.

  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.openhab.binding.sectoralarm.internal.model.AlarmSystem;
import org.openhab.binding.sectoralarm.internal.model.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The {@link SectorAlarmService} handles all communication with the external alarm service
 * interface, in this case a web based application.
 * <p>
 * Request URL:https://minasidor.sectoralarm.se/Panel/GetTempratures/02658648
 * Request Method:GET
 *
 * @author Henrik Svensson
 */
public class SectorAlarmService {

    private final Logger logger = LoggerFactory.getLogger(SectorAlarmService.class);

    static int timeout = 10000; // 10 seconds in milliseconds

    private String username;
    private String password;
    private String alarmSystemCode;
    private String baseUrl;

    private Map<String, String> loginCookies;
    private LocalDateTime loginCookiesAge;
    private Gson gson;

    public SectorAlarmService(String username, String password, String alarmSystemCode, String baseUrl) {
        this.username = username;
        this.password = password;
        this.alarmSystemCode = alarmSystemCode;
        this.baseUrl = baseUrl;

        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingStrategy(f -> Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1));
        gson = builder.create();
    }

    /**
     * Returns a {@link AlarmSystem} object filled with updated information on
     * the complete alarm system. Note that the temperature values are not included
     * in the result unless the includeTemperatures parameter is set to true. The
     * devices themselves (thermometers) are included, but not the thermometer values.
     *
     * @param includeTemperatures true if the temperatures should be included in the
     *                            result.
     * @return The AlarmSystem
     */
    public AlarmSystem getAlarmSystem(boolean includeTemperatures) {
        String resultJson = null;
        try {
            // @formatter:off
            resultJson = Jsoup.connect(baseUrl + "/Panel/GetOverview")
                    .data("PanelId", alarmSystemCode)
                    .cookies(getLoginCookies())
                    .timeout(timeout)
                    .method(Method.POST)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            // @formatter:on

        } catch (IOException e) {
            logger.error("Failed to get information from SectorAlarm.", e);
        }

        AlarmSystem alarmSystem = gson.fromJson(resultJson, AlarmSystem.class);

        if (includeTemperatures)
            alarmSystem.temperatures = getTemperatures();

        return alarmSystem;
    }

    /**
     * Gets an updated list of the temperatures including the thermometer values in
     * degrees centigrade.
     *
     * @return A list of Temperature objects belonging to the alarm system
     */
    public List<Temperature> getTemperatures() {
        String resultJson = null;
        try {
            // @formatter:off
            resultJson = Jsoup.connect(baseUrl + "/Panel/GetTempratures/" + alarmSystemCode)
                    .cookies(getLoginCookies())
                    .timeout(timeout)
                    .method(Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            // @formatter:on

        } catch (IOException e) {
            logger.error("Failed to get information from SectorAlarm.", e);
        }

        return gson.fromJson(resultJson, new TypeToken<List<Temperature>>() {
        }.getType());
    }

    protected Map<String, String> getLoginCookies() throws IOException {

        if (areLoginCookiesValid())
            return loginCookies;

        // @formatter:off
        Response loginResponse = Jsoup.connect(baseUrl + "/User/Login")
                .data("userID", username)
                .data("password", password)
                .method(Method.POST)
                .timeout(timeout)
                .execute();
        // @formatter:on

        loginCookies = loginResponse.cookies();
        loginCookiesAge = LocalDateTime.now();

        return loginCookies;
    }

    protected boolean areLoginCookiesValid() {
        return loginCookies != null && loginCookiesAge != null &&
                loginCookiesAge.plusMinutes(30).isAfter(LocalDateTime.now());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
