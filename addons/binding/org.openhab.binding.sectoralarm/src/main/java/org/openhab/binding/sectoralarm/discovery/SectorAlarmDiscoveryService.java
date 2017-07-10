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
import java.util.Random;
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
        sectorAlarmService = new SectorAlarmService(
                bridgeHandler.getUsername(),
                bridgeHandler.getPassword(),
                bridgeHandler.getAlarmSystemCode(),
                bridgeHandler.getBaseUrl());
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
        logger.debug("*** getSupportedThingTypes");
        return SectorAlarmBindingConstants.SUPPORTED_THING_TYPES_UIDS;
    }

    @Override
    protected void startScan() {
        logger.debug("*** startScan");
        String tempId = String.valueOf(new Random().nextInt());

        for (SectorAlarmBridgeHandler bridgeHandler : bridgeHandlers) {
            logger.debug("TODO: start scan for sector alarm things");
            ThingUID thingUID = new ThingUID(SectorAlarmBindingConstants.THING_TYPE_THERMOMETER, "therm" + tempId);
            if (thingUID != null) {
                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withTTL(DEFAULT_TTL)
                        // .withProperty(TellstickBindingConstants.DEVICE_ID, device.getUUId())
                        .withBridge(bridgeHandler.getThing().getBridgeUID())
                        .withLabel("Discovery test thermometer " + tempId).build();
                thingDiscovered(discoveryResult);

            } else {
                logger.warn("Discovered unsupported Sector Alarm device.");
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
