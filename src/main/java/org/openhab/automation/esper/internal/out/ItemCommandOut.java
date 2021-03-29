package org.openhab.automation.esper.internal.out;

import org.openhab.core.events.Event;
import org.openhab.core.items.events.ItemEventFactory;
import org.openhab.core.types.Command;

public class ItemCommandOut extends ItemOut {
    private final Command command;

    public ItemCommandOut(String itemName, Command command) {
        super(itemName);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public Event toInEvent() {
        return ItemEventFactory.createCommandEvent(getItemName(), command);
    }
}
