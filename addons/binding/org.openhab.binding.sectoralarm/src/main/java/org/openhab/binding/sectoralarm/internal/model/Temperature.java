/*
  Copyright (c) 2010-2017 by the respective copyright holders.

  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sectoralarm.internal.model;

/**
 * Representation of the SectorAlarm data model.
 *
 * @author Henrik Svensson
 */
public class Temperature {
    public String label;
    public String serialNo;
    public String temprature; // Note that the name is misspelled in the JSON response from the SectorAlarm REST service.
    public String deviceId;
}
