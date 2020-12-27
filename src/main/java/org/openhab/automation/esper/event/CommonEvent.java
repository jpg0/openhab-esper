package org.openhab.automation.esper.event;

import java.util.Date;

abstract class CommonEvent {
    String type;
    Date time;
    String name;
}
