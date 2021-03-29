package org.openhab.automation.esper.epl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import javax.script.*;

import org.apache.commons.io.IOUtils;
import org.openhab.automation.esper.EsperEngine;

public class EPLScriptEngine extends AbstractScriptEngine implements Invocable {

    private final EsperEngine esperEngine;
    private EsperEngine.Deployment unloadHook;

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
                throw new IllegalStateException("Not Implemented");
        }

        return null;
    }

    private void unload() {
        unloadHook.dispose();
        unloadHook = null;
    }

    @Override
    public Object invokeMethod(Object o, String s, Object... objects) throws ScriptException, NoSuchMethodException {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public <T> T getInterface(Class<T> aClass) {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public <T> T getInterface(Object o, Class<T> aClass) {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public Bindings createBindings() {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return new ScriptEngineFactory() {
            @Override
            public String getEngineName() {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public String getEngineVersion() {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public List<String> getExtensions() {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public List<String> getMimeTypes() {
                return Collections.singletonList("application/epl");
            }

            @Override
            public List<String> getNames() {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public String getLanguageName() {
                return "Event Processing Language";
            }

            @Override
            public String getLanguageVersion() {
                return "8.6.0"; // todo: pull from manifest
            }

            @Override
            public Object getParameter(String s) {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public String getMethodCallSyntax(String s, String s1, String... strings) {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public String getOutputStatement(String s) {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public String getProgram(String... strings) {
                throw new IllegalStateException("Not Implemented");
            }

            @Override
            public ScriptEngine getScriptEngine() {
                throw new IllegalStateException("Not Implemented");
            }
        };
    }
}
