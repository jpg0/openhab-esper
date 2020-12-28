/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.automation.esper.epl;

import java.util.*;

import javax.script.ScriptEngine;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.automation.esper.EsperEngine;
import org.openhab.core.automation.module.script.ScriptEngineFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * An implementation of {@link ScriptEngineFactory} for EPL scripts.
 *
 * @author Jonathan Gilbert - Initial contribution
 */
@NonNullByDefault
@Component(service = ScriptEngineFactory.class)
public final class EPLScriptEngineFactory implements ScriptEngineFactory {

    private EsperEngine esperEngine;

    @Activate
    public EPLScriptEngineFactory(final @Reference EsperEngine esperEngine) {
        this.esperEngine = esperEngine;
    }

    @Override
    public List<String> getScriptTypes() {
        return Arrays.asList("epl", "application/epl");
    }

    @Override
    public void scopeValues(ScriptEngine scriptEngine, Map<String, Object> scopeValues) {
        // not implemented
    }

    @Override
    public @Nullable ScriptEngine createScriptEngine(String scriptType) {
        return new EPLScriptEngine(esperEngine);
    }
}
