package org.openhab.automation.esper.internal.out;

import org.openhab.core.model.script.actions.BusEvent;
import org.openhab.core.types.State;

public class ItemStateOut extends ItemOut {
    private String stringState;

    public ItemStateOut(String itemName, State state) {
        super(itemName);
        this.stringState = state.toFullString();
    }

    public ItemStateOut(String itemName, String state) {
        super(itemName);
        this.stringState = state;
    }

    @Override
    public void sendEvent() {
        BusEvent.postUpdate(getItemName(), stringState);
    }

    @Override
    public String toString() {
        return "ItemStateOut{" +
                "itemName='" + getItemName() + '\'' +
                "stringState='" + stringState + '\'' +
                '}';
    }
}
