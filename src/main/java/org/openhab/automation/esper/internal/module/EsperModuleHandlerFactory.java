package org.openhab.automation.esper.internal.module;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.automation.esper.internal.EsperEngine;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.Trigger;
import org.openhab.core.automation.handler.BaseModuleHandlerFactory;
import org.openhab.core.automation.handler.ModuleHandler;
import org.openhab.core.automation.handler.ModuleHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
@Component(service = ModuleHandlerFactory.class)
public class EsperModuleHandlerFactory extends BaseModuleHandlerFactory {
    private final Logger logger = LoggerFactory.getLogger(EsperModuleHandlerFactory.class);

    private static final Collection<String> TYPES = Arrays
            .asList(new String[] { EPLExpressionTriggerHandler.MODULE_TYPE_ID });

    private EsperEngine esperEngine;

    @Activate
    public EsperModuleHandlerFactory(@Reference EsperEngine esperEngine) {
        this.esperEngine = esperEngine;
    }

    @Deactivate
    public void deactivate() {
        super.deactivate();
    }

    public Collection<String> getTypes() {
        return TYPES;
    }

    @Override
    protected @Nullable ModuleHandler internalCreate(Module module, String ruleUID) {
        this.logger.trace("create {} -> {}", module.getId(), module.getTypeUID());
        String moduleTypeUID = module.getTypeUID();
        if (EPLExpressionTriggerHandler.MODULE_TYPE_ID.equals(moduleTypeUID) && module instanceof Trigger) {
            return new EPLExpressionTriggerHandler((Trigger) module, esperEngine);
        } else {
            this.logger.error("The module handler type '{}' is not supported.", moduleTypeUID);
            return null;
        }
    }
}
