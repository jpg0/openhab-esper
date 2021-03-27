package org.openhab.automation.esper;

import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.events.Event;
import org.openhab.core.items.events.ItemCommandEvent;
import org.openhab.core.items.events.ItemStateChangedEvent;
import org.openhab.core.items.events.ItemStateEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.*;
import com.espertech.esper.runtime.client.*;

@NonNullByDefault
@Component(service = EsperEngine.class)
public class EsperEngine {
    private final Logger logger = LoggerFactory.getLogger(EsperEngine.class);

    private final EPRuntime runtime;
    private final Configuration configuration;

    @Activate
    public EsperEngine() throws NoSuchFieldException, IllegalAccessException {
        configuration = new Configuration();

        for (Class<? extends Event> eventClass : eventTypes()) {
            configuration.getCommon().addEventType((String) eventClass.getField("TYPE").get(null), eventClass);
        }

        configuration.getCompiler().getByteCode().setAllowSubscriber(true);

        runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
        logger.info("Created Esper runtime");
    }

    @Deactivate
    public void dispose() {
        runtime.destroy();
        logger.info("Destroyed Esper runtime");
    }

    public static Set<Class<? extends Event>> eventTypes() {
        return Set.of(ItemStateEvent.class, ItemStateChangedEvent.class, ItemCommandEvent.class);
    }

    public void sendEvent(Event event) {
        if (eventTypes().contains(event.getClass())) {
            logger.trace("Sending event of type {} to Esper", event.getType());
            getRuntime().getEventService().sendEventBean(event, event.getType());
        } else {
            logger.trace("Not sending event of type {} to Esper", event.getType());
        }
    }

    public EPRuntime getRuntime() {
        return runtime;
    }

    public Runnable deployEPL(String epl, @Nullable Consumer<Object> callback) {
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        CompilerArguments args = new CompilerArguments(configuration);

        EPCompiled epCompiled;
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            epCompiled = compiler.compile(epl, args);
        } catch (EPCompileException ex) {
            throw new IllegalArgumentException("Failed to compile EPL", ex);
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }

        EPDeployment deployment;
        try {
            deployment = runtime.getDeploymentService().deploy(epCompiled);
        } catch (EPDeployException ex) {
            throw new IllegalStateException("Failed to deploy EPL", ex);
        }

        if (callback != null) {
            for (EPStatement statement : deployment.getStatements()) {
                statement.setSubscriber(new Object() {
                    public void update(Object o) {
                        callback.accept(o);
                    }
                });
            }
        }

        return () -> {
            try {
                runtime.getDeploymentService().undeploy(deployment.getDeploymentId());
            } catch (EPUndeployException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
