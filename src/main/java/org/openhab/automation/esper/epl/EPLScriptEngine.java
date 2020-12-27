package org.openhab.automation.esper.epl;

import java.io.IOException;
import java.io.Reader;

import javax.script.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.openhab.automation.esper.EsperEngine;

public class EPLScriptEngine extends AbstractScriptEngine {

    private final EsperEngine esperEngine;

    public EPLScriptEngine(EsperEngine esperEngine) {
        super();
        this.esperEngine = esperEngine;
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        esperEngine.deployEPL(s);
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
    public Bindings createBindings() {
        throw new NotImplementedException();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        throw new NotImplementedException();
    }
}
