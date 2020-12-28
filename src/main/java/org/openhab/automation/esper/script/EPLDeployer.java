package org.openhab.automation.esper.script;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public interface EPLDeployer {
    void deploy(String epl, @Nullable Consumer<Object> callback);
}
