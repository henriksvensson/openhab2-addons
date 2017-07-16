/*
  Copyright (c) 2010-2017 by the respective copyright holders.

  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link SectorAlarmBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Henrik Svensson - Initial contribution
 */
public class SectorAlarmBindingConstants {

    private static final String BINDING_ID = "sectoralarm";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_THERMOMETER = new ThingTypeUID(BINDING_ID, "thermometer");
    public static final ThingTypeUID THING_TYPE_ALARM_SYSTEM = new ThingTypeUID(BINDING_ID, "alarmsystem");

    // List of all Channel ids
    public static final String CHANNEL_TIMESTAMP = "timestamp";
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_ALARM_STATE = "alarmstate";

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(THING_TYPE_THERMOMETER, THING_TYPE_ALARM_SYSTEM).collect(Collectors.toSet());

    public static final Set<ThingTypeUID> DISCOVERABLE_THING_TYPES_UIDS = Stream.of(THING_TYPE_THERMOMETER)
            .collect(Collectors.toSet());

}
