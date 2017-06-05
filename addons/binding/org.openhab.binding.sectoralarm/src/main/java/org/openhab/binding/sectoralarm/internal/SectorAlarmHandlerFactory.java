/*
  Copyright (c) 2010-2017 by the respective copyright holders.

  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm.internal;

import static org.openhab.binding.sectoralarm.SectorAlarmBindingConstants.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.sectoralarm.handler.SectorAlarmAlarmSystemHandler;
import org.openhab.binding.sectoralarm.handler.SectorAlarmThermometerHandler;

/**
 * The {@link SectorAlarmHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Henrik Svensson - Initial contribution
 */
public class SectorAlarmHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(THING_TYPE_THERMOMETER, THING_TYPE_ALARM_SYSTEM).collect(Collectors.toSet());

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_THERMOMETER)) {
            return new SectorAlarmThermometerHandler(thing);
        }

        if (thingTypeUID.equals(THING_TYPE_ALARM_SYSTEM)) {
            return new SectorAlarmAlarmSystemHandler(thing);
        }

        return null;
    }
}
