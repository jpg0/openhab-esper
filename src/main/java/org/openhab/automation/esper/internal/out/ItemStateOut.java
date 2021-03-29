package org.openhab.automation.esper.internal.out;

import org.openhab.core.events.Event;
import org.openhab.core.items.events.ItemEventFactory;
import org.openhab.core.types.State;

public class ItemStateOut extends ItemOut {
    private State state;

    public ItemStateOut(String itemName, State state) {
        super(itemName);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Override
    public Event toInEvent() {
        return ItemEventFactory.createStateEvent(getItemName(), state);
    }
}
