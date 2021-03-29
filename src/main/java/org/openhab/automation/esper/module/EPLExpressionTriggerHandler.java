package org.openhab.automation.esper.module;

import java.util.Collections;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.automation.esper.EsperEngine;
import org.openhab.core.automation.ModuleHandlerCallback;
import org.openhab.core.automation.Trigger;
import org.openhab.core.automation.handler.BaseTriggerModuleHandler;
import org.openhab.core.automation.handler.TriggerHandlerCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class EPLExpressionTriggerHandler extends BaseTriggerModuleHandler {

    private final Logger logger = LoggerFactory.getLogger(EPLExpressionTriggerHandler.class);
    public static final String MODULE_TYPE_ID = "esper.EPLExpressionTrigger";
    public static final String CONFIG_KEY_EXPRESSION = "expression";

    private final String expression;
    private final EsperEngine esperEngine;

    @NonNullByDefault({})
    private EsperEngine.Deployment deploymentShutdownHook;

    public EPLExpressionTriggerHandler(Trigger module, EsperEngine esperEngine) {
        super(module);
        this.esperEngine = esperEngine;
        this.expression = (String) module.getConfiguration().get(CONFIG_KEY_EXPRESSION);
    }

    public synchronized void setCallback(ModuleHandlerCallback callback) {
        super.setCallback(callback);
        deploymentShutdownHook = esperEngine.deployEPL(this.expression, event -> ((TriggerHandlerCallback) callback)
                .triggered(module, Collections.singletonMap("event", event)));
        this.logger.debug("started EPL listener for trigger '{}'.", this.module.getId());
    }

    @NonNullByDefault({})
    public synchronized void dispose() {
        super.dispose();
        EsperEngine.Deployment disposeHook = this.deploymentShutdownHook;
        if (disposeHook != null) {
            disposeHook.dispose();
            this.logger.debug("shutdown EPL listener for trigger '{}'.", this.module.getId());
        }
    }
}
