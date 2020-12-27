package org.openhab.automation.esper;

import org.openhab.core.items.events.ItemCommandEvent;
import org.openhab.core.items.events.ItemStateChangedEvent;
import org.openhab.core.items.events.ItemStateEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;

@Component
public class EsperEngine {
    private Logger logger = LoggerFactory.getLogger(EsperEngine.class);

    private EPRuntime runtime;
    private Configuration configuration;

    @Activate
    public void activate() {
        configuration = new Configuration();
        configuration.getCommon().addEventType(ItemStateEvent.TYPE, ItemStateEvent.class);
        configuration.getCommon().addEventType(ItemStateChangedEvent.TYPE, ItemStateChangedEvent.class);
        configuration.getCommon().addEventType(ItemCommandEvent.TYPE, ItemCommandEvent.class);

        runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
        logger.info("Created Esper provider");
    }

    public EPRuntime getRuntime() {
        return runtime;
    }

    public Runnable deployEPL(String epl) {
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        CompilerArguments args = new CompilerArguments(configuration);

        EPCompiled epCompiled;
        try {
            epCompiled = compiler.compile(epl, args);
        } catch (EPCompileException ex) {
            throw new IllegalArgumentException("Failed to compile EPL", ex);
        }

        EPDeployment deployment;
        try {
            deployment = runtime.getDeploymentService().deploy(epCompiled);
        } catch (EPDeployException ex) {
            throw new IllegalStateException("Failed to deploy EPL", ex);
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
