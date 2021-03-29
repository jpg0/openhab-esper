package org.openhab.automation.esper.script;

import java.util.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.automation.esper.EsperEngine;
import org.openhab.core.automation.module.script.ScriptExtensionProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@NonNullByDefault
@Component(immediate = true, service = ScriptExtensionProvider.class)
public class EPLScriptExtensionProvider implements ScriptExtensionProvider {

    public static final String NAME = "epl";
    public static final String DEPLOYER_TYPE_NAME = "deployer";

    private final EsperEngine esperEngine;

    private Map<String, List<EsperEngine.Deployment>> scriptIdentifierToUnloadHooks = new HashMap<>();

    @Activate
    public EPLScriptExtensionProvider(final @Reference EsperEngine esperEngine) {
        this.esperEngine = esperEngine;
    }

    @Override
    public Collection<String> getDefaultPresets() {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> getPresets() {
        return Collections.singleton(NAME);
    }

    @Override
    public Collection<String> getTypes() {
        return Collections.singleton(DEPLOYER_TYPE_NAME);
    }

    @Override
    public @Nullable Object get(String type, String scriptIdentifier) throws IllegalArgumentException {
        if (DEPLOYER_TYPE_NAME.equals(type)) {
            return getDeployer(scriptIdentifier);
        }

        return null;
    }

    private EPLDeployer getDeployer(String scriptIdentifier) {
        return (epl, callback) -> {
            EsperEngine.Deployment deployment = esperEngine.deployEPL(epl, callback);
            scriptIdentifierToUnloadHooks.putIfAbsent(scriptIdentifier, new ArrayList<>());
            List<EsperEngine.Deployment> unloadHooks = scriptIdentifierToUnloadHooks.get(scriptIdentifier);
            unloadHooks.add(deployment);
        };
    }

    @Override
    public Map<String, Object> importPreset(String scriptIdentifier, String preset) {
        if (NAME.equals(preset)) {
            return Map.of(DEPLOYER_TYPE_NAME, getDeployer(scriptIdentifier));
        }

        return Map.of();
    }

    @Override
    public void unload(String scriptIdentifier) {
        List<EsperEngine.Deployment> unloadHooks = scriptIdentifierToUnloadHooks.remove(scriptIdentifier);

        if (unloadHooks != null) {
            for (EsperEngine.Deployment unloadHook : unloadHooks) {
                unloadHook.dispose();
            }
        }
    }
}
