package org.openhab.automation.esper.internal.bridge;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.automation.esper.internal.EsperEngine;
import org.openhab.core.events.Event;
import org.openhab.core.events.EventFilter;
import org.openhab.core.events.EventSubscriber;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = EventSubscriber.class)
public class EsperEventSubscriber implements EventSubscriber {

    private final EsperEngine esperEngine;

    @Activate
    public EsperEventSubscriber(final @Reference EsperEngine esperEngine) {
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
        esperEngine.sendEvent(event);
    }
}
