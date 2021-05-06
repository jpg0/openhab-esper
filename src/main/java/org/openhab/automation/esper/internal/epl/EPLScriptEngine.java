package org.openhab.automation.esper.internal.epl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.openhab.automation.esper.internal.EsperEngine;

public class EPLScriptEngine extends AbstractScriptEngine implements Invocable {

    private final EsperEngine esperEngine;
    private EsperEngine.Deployment deployment;

    public EPLScriptEngine(EsperEngine esperEngine) {
        super();
        this.esperEngine = esperEngine;
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        deployment = esperEngine.deployEPL(s, null);
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        try {
            return eval(readAll(reader), scriptContext);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    private static String readAll(Reader reader) throws IOException {
        char[] arr = new char[8 * 1024];
        StringBuilder buffer = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
            buffer.append(arr, 0, numCharsRead);
        }
        reader.close();
        return buffer.toString();
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
        if (deployment != null) {
            deployment.dispose();
            deployment = null;
        }
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
