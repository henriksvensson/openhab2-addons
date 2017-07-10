/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm;

/**
 * Configuration class for {@link SectorAlarmBridge} bridge used to connect to the
 * Sector Alarm web page
 *
 * @author Henrik Svensson - Initial contribution
 */
public class SectorAlarmBridgeConfiguration {
    public String email;
    public String password;
    public String alarmSystemCode;
    public String baseUrl;
}
