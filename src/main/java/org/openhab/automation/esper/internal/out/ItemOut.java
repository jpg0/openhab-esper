package org.openhab.automation.esper.internal.out;

public abstract class ItemOut {
    private final String itemName;

    public ItemOut(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public abstract void sendEvent();
}
