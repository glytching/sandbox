package org.glytching.sandbox.mockito;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.listeners.MockCreationListener;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MockitoTest {

    @Test
    public void listAllMocks() {
        List<Object> mocks = new ArrayList<>();

        Mockito.framework().addListener((MockCreationListener) (mock, settings) -> mocks.add(mock));

        A a = Mockito.mock(A.class);
        B b = Mockito.mock(B.class);

        // ... do something with a, b

        // verify
        assertThat(mocks.size(), is(2));
        assertThat(mocks, hasItem(a));
        assertThat(mocks, hasItem(b));
    }
}