package org.openhab.automation.esper.out;

import org.openhab.core.events.Event;

public abstract class ItemOut {
    private final String itemName;

    public ItemOut(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public abstract Event toInEvent();
}
