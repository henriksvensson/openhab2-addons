/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm.discovery;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.sectoralarm.SectorAlarmBindingConstants;
import org.openhab.binding.sectoralarm.handler.SectorAlarmBridgeHandler;
import org.openhab.binding.sectoralarm.internal.SectorAlarmService;
import org.openhab.binding.sectoralarm.internal.model.AlarmSystem;
import org.openhab.binding.sectoralarm.internal.model.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovery Service class for {@link SectorAlarmBridgeHandler} used to discover
 * Things attached to a given alarm system.
 *
 * @author Henrik Svensson - Initial contribution
 */
public class SectorAlarmDiscoveryService extends AbstractDiscoveryService {
    private static final long DEFAULT_TTL = 60 * 60; // 1 Hour

    private final Logger logger = LoggerFactory.getLogger(SectorAlarmDiscoveryService.class);

    private List<SectorAlarmBridgeHandler> bridgeHandlers = new Vector<>();

    private SectorAlarmService sectorAlarmService;

    public SectorAlarmDiscoveryService(int timeout) throws IllegalArgumentException {
        super(timeout);
    }

    public SectorAlarmDiscoveryService(SectorAlarmBridgeHandler bridgeHandler) {
        super(SectorAlarmBindingConstants.SUPPORTED_THING_TYPES_UIDS, 10, true);
        this.bridgeHandlers.add(bridgeHandler);
        sectorAlarmService = new SectorAlarmService(bridgeHandler.getUsername(), bridgeHandler.getPassword(),
                bridgeHandler.getAlarmSystemCode(), bridgeHandler.getBaseUrl());
    }

    public void activate() {
        logger.debug("*** activate");
        // for (SectorAlarmBridgeHandler bridgeHandler : bridgeHandlers) {
        // bridgeHandler.registerDeviceStatusListener(this);
        // }
    }

    @Override
    public void deactivate() {
        logger.debug("*** deactivate");
        // for (SectorAlarmBridgeHandler bridgeHandler : bridgeHandlers) {
        // bridgeHandler.unregisterDeviceStatusListener(this);
        // }
    }

    public void addBridgeHandler(SectorAlarmBridgeHandler bridgeHandler) {
        logger.debug("*** addBridgeHandler");
        bridgeHandlers.add(bridgeHandler);
        // bridgeHandler.registerDeviceStatusListener(this);
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return SectorAlarmBindingConstants.DISCOVERABLE_THING_TYPES_UIDS;
    }

    @Override
    protected void startScan() {
        logger.debug("Scanning for SectorAlarm things.");
        for (SectorAlarmBridgeHandler bridgeHandler : bridgeHandlers) {
            AlarmSystem alarmSystem = sectorAlarmService.getAlarmSystem(true);

            for (Temperature temperature : alarmSystem.temperatures) {
                ThingUID thingUID = new ThingUID(SectorAlarmBindingConstants.THING_TYPE_THERMOMETER,
                        temperature.serialNo);

                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withTTL(DEFAULT_TTL)
                        // .withProperty(TellstickBindingConstants.DEVICE_ID, device.getUUId())
                        .withBridge(bridgeHandler.getThing().getBridgeUID()).withLabel(temperature.label).build();
                thingDiscovered(discoveryResult);
            }
        }
    }

    @Override
    public boolean isBackgroundDiscoveryEnabled() {
        logger.debug("*** isBackgroundDiscoveryEnabled");
        return true;
    }

    @Override
    public void startBackgroundDiscovery() {
        logger.debug("*** startBackgroundDiscovery");
    }

    @Override
    public void stopBackgroundDiscovery() {
        logger.debug("*** stopBackgroundDiscovery");
    }

}
