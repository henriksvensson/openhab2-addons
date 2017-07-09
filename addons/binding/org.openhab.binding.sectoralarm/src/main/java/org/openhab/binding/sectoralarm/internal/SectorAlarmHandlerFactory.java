/*
  Copyright (c) 2010-2017 by the respective copyright holders.

  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.sectoralarm.SectorAlarmBindingConstants;
import org.openhab.binding.sectoralarm.discovery.SectorAlarmDiscoveryService;
import org.openhab.binding.sectoralarm.handler.SectorAlarmBridgeHandler;
import org.openhab.binding.sectoralarm.handler.SectorAlarmThermometerHandler;
import org.osgi.service.component.ComponentContext;

/**
 * The {@link SectorAlarmHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Henrik Svensson
 */
public class SectorAlarmHandlerFactory extends BaseThingHandlerFactory {

    private SectorAlarmDiscoveryService discoveryService = null;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SectorAlarmBindingConstants.SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected void activate(ComponentContext componentContext) {
        super.activate(componentContext);
        Dictionary<String, Object> properties = componentContext.getProperties();
        String email = (String) properties.get("email");
        String password = (String) properties.get("password");
        String baseUrl = (String) properties.get("baseUrl");
    };

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(SectorAlarmBindingConstants.THING_TYPE_THERMOMETER)) {
            return new SectorAlarmThermometerHandler(thing);
        }

        if (thingTypeUID.equals(SectorAlarmBindingConstants.THING_TYPE_ALARM_SYSTEM)) {
            SectorAlarmBridgeHandler handler = new SectorAlarmBridgeHandler((Bridge) thing);
            registerDeviceDiscoveryService(handler);
            return handler;
        }

        return null;
    }

    private void registerDeviceDiscoveryService(SectorAlarmBridgeHandler bridgeHandler) {
        if (discoveryService == null) {
            discoveryService = new SectorAlarmDiscoveryService(bridgeHandler);
            discoveryService.activate();
            bundleContext.registerService(DiscoveryService.class.getName(), discoveryService,
                    new Hashtable<>());
        } else {
            discoveryService.addBridgeHandler(bridgeHandler);
        }
    }

}
