package org.openhab.automation.esper.epl;

import java.io.IOException;
import java.io.Reader;

import javax.script.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.openhab.automation.esper.EsperEngine;

public class EPLScriptEngine extends AbstractScriptEngine implements Invocable {

    private final EsperEngine esperEngine;
    private Runnable unloadHook;

    public EPLScriptEngine(EsperEngine esperEngine) {
        super();
        this.esperEngine = esperEngine;
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        unloadHook = esperEngine.deployEPL(s, null);
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        try {
            return eval(IOUtils.toString(reader), scriptContext);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Object invokeFunction(String s, Object... objects) throws ScriptException, NoSuchMethodException {

        switch (s) {
            case "scriptLoaded":
                break;
            case "scriptUnloaded":
                unload();
                break;
            default:
                throw new NotImplementedException();
        }

        return null;
    }

    private void unload() {
        unloadHook.run();
        unloadHook = null;
    }

    @Override
    public Object invokeMethod(Object o, String s, Object... objects) throws ScriptException, NoSuchMethodException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getInterface(Class<T> aClass) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getInterface(Object o, Class<T> aClass) {
        throw new NotImplementedException();
    }

    @Override
    public Bindings createBindings() {
        throw new NotImplementedException();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        throw new NotImplementedException();
    }
}
