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
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.openhab.binding.sectoralarm.internal.model.AlarmSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The {@link SectorAlarmService} handles all communication with the external alarm service
 * interface, in this case a web based application.
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

    public SectorAlarmService(String username, String password, String alarmSystemCode, String baseUrl) {
        this.username = username;
        this.password = password;
        this.alarmSystemCode = alarmSystemCode;
        this.baseUrl = baseUrl;
    }

    public AlarmSystem getAlarmSystem() {
        String overviewJson = null;
        try {
            // @formatter:off
            Response loginResponse = Jsoup.connect(baseUrl + "/User/Login")
                    .data("userID", username)
                    .data("password", password)
                    .method(Method.POST)
                    .timeout(timeout)
                    .execute();
            // @formatter:on

            // @formatter:off
            overviewJson = Jsoup.connect(baseUrl + "/Panel/GetOverview")
                    .data("PanelId", alarmSystemCode)
                    .cookies(loginResponse.cookies())
                    .timeout(timeout)
                    .method(Method.POST)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            // @formatter:on

        } catch (IOException e) {
            logger.error("Failed getting information from SectorAlarm.", e);
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingStrategy(f -> Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1));
        Gson gson = builder.create();

        return gson.fromJson(overviewJson, AlarmSystem.class);
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
