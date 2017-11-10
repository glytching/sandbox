package org.glytching.sandbox.junit.runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class IgnoreBeforeAndAfterRunner extends BlockJUnit4ClassRunner {

    public IgnoreBeforeAndAfterRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        return statement;
    }

    protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
        return statement;
    }
}