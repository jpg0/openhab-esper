package org.openhab.automation.esper.internal.out;

import org.openhab.core.model.script.actions.BusEvent;
import org.openhab.core.types.Command;

public class ItemCommandOut extends ItemOut {
    private final String stringCommand;

    public ItemCommandOut(String itemName, Command command) {
        super(itemName);
        this.stringCommand = command.toFullString();
    }

    public ItemCommandOut(String itemName, String command) {
        super(itemName);
        this.stringCommand = command;
    }

    @Override
    public void sendEvent() {
        BusEvent.sendCommand(getItemName(), stringCommand);
    }
}
