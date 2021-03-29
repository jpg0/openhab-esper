package org.openhab.automation.esper.bridge;

import com.espertech.esper.common.internal.event.bean.core.BeanEventBean;
import org.openhab.automation.esper.EsperEngine;
import org.openhab.automation.esper.out.ItemOut;
import org.openhab.core.events.EventPublisher;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EsperEventPublisher.class, immediate = true)
public class EsperEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(EsperEventPublisher.class);

    private final EsperEngine esperEngine;
    private final EventPublisher eventPublisher;

    private EsperEngine.Deployment listenDisposer;

    @Activate
    public EsperEventPublisher(@Reference EsperEngine esperEngine, @Reference EventPublisher eventPublisher) {
        this.esperEngine = esperEngine;
        this.eventPublisher = eventPublisher;
    }

    private String getSubscriptionExpression() {
        StringBuilder rv = new StringBuilder();

        for (Class<?> outEventType : EsperEngine.outEventTypes()) {
            rv.append("select * from ").append(outEventType.getSimpleName()).append("\n");
        }

        return rv.toString();
    }

    @Activate
    public void startListening() {
        String exp = getSubscriptionExpression();
        logger.debug("Subscribing to Esper streams to bridge onto OH streams with " + exp);
        listenDisposer = esperEngine.deployEPL(exp, out -> onEvent(coerceToItemOut(out)));
    }

    @Deactivate
    public void stopListening() {
        listenDisposer.dispose();
    }

    private ItemOut coerceToItemOut(Object o) {
        if (o instanceof BeanEventBean) {
            o = ((BeanEventBean) o).getUnderlying();
        }

        if (o instanceof ItemOut) {
            return (ItemOut) o;
        }

        throw new IllegalArgumentException("Cannot coerce " + o.getClass().getName() + " to ItemOut");
    }

    public void onEvent(ItemOut outEvent) {
        logger.debug("Bridging " + outEvent.toString());
        eventPublisher.post(outEvent.toInEvent());
    }
}
