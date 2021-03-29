package org.openhab.automation.esper;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public interface EPLDeployer {
    Deployment deployEPL(String epl, @Nullable Consumer<Object> callback);

    interface Deployment {
        void dispose();
    }
}
