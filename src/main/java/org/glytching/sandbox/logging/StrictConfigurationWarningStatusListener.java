package org.glytching.sandbox.logging;

import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;

public class StrictConfigurationWarningStatusListener implements StatusListener {
    @Override
    public void addStatusEvent(Status status) {
        if (status.getEffectiveLevel() == Status.WARN) {
            // might want to consider how best to evaluate whether this is the relevant event
            // this approach is bound to a string and hence will no longer work if Logback changes this event message
            if (status.getMessage().endsWith("occurs multiple times on the classpath.")) {
                throw new LogbackException(status.getMessage());
            }
        }
    }
}
