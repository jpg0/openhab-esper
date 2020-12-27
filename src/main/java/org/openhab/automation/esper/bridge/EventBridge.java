package org.openhab.automation.esper.bridge;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.automation.esper.EsperEngine;
import org.openhab.core.events.Event;
import org.openhab.core.events.EventFilter;
import org.openhab.core.events.EventSubscriber;
import org.osgi.service.component.annotations.Component;

@Component
public class EventBridge implements EventSubscriber {

    private EsperEngine esperEngine;

    public EventBridge(EsperEngine esperEngine) {
        this.esperEngine = esperEngine;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return Collections.singleton(ALL_EVENT_TYPES);
    }

    @Override
    public @Nullable EventFilter getEventFilter() {
        return null;
    }

    @Override
    public void receive(Event event) {
        esperEngine.getRuntime().getEventService().sendEventBean(event, event.getType());
    }
}
