package org.glytching.sandbox.janino;

import ch.qos.logback.core.joran.conditional.Condition;
import ch.qos.logback.core.joran.conditional.PropertyWrapperForScripts;
import org.codehaus.janino.ClassBodyEvaluator;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JaninoTest {

    @Test
    public void canCompile() throws Exception {
        String script = "public boolean evaluate() { return \"test\".equals(\"test\"); }";

        ClassBodyEvaluator classBodyEvaluator = new ClassBodyEvaluator();
        classBodyEvaluator.setImplementedInterfaces(new Class[]{Condition.class});
        classBodyEvaluator.setExtendedClass(PropertyWrapperForScripts.class);
        classBodyEvaluator.setParentClassLoader(ClassBodyEvaluator.class.getClassLoader());

        StringReader sr = new StringReader(script);
        classBodyEvaluator.cook(null, sr);
        Class clazz = classBodyEvaluator.getClazz();
        Condition instance = (Condition) clazz.newInstance();

        assertTrue(instance.evaluate());
    }
}