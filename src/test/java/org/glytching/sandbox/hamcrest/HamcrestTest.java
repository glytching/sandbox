package org.glytching.sandbox.hamcrest;

import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HamcrestTest {

    @Test
    public void canAssertUsingACustomMatcher() {
        List<HamcrestSUT> incoming = Lists.newArrayList(
                new HamcrestSUT(1, "bill", 5d), new HamcrestSUT(2, "bob", 6d), new HamcrestSUT(3, "jocasta", 7d)
        );

        assertThat(incoming, hasItem(HamcrestSUT.CustomMatcher.matches("bob", 2)));
    }

    @Test
    public void canAssertUsingCombinedMatchers() throws InterruptedException {
        int anInt = 5;
        String aString = "any value";
        List<String> aCollection = Lists.newArrayList("a", "b", "c");

        assertThat(anInt, is(5));
        assertThat(anInt, greaterThan(4));
        assertThat(anInt, lessThan(6));
        assertThat(anInt, both(greaterThan(3)).and(lessThanOrEqualTo(5)));

        assertThat(aString, is("any value"));

        assertThat(aString, is("any value"));

        assertThat(aCollection, hasSize(3));
        assertThat(aCollection, hasItem("a"));
        assertThat(new HamcrestSUT(1, "me", 1.1d), isEquivalent(1, "me"));
        assertThat(Lists.newArrayList(new HamcrestSUT(1, "me", 1.1d)), hasItem(isEquivalent(1, "me")));
    }

    @Test
    public void testWithObjectArray() {
        HamcrestSUT mock = Mockito.mock(HamcrestSUT.class);

        mock.getMessage("a code", new Object[]{"", "", "", "", "", "", "", "", "", "CHF"}, Locale.CANADA);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(mock).getMessage(Mockito.anyString(), (Object[]) captor.capture(), Mockito.any(Locale.class));
        Mockito.verify(mock).getMessage(Mockito.anyString(), (Object[]) captor.capture(), Mockito.any(Locale.class));

        List<Object> actualArgs = Arrays.asList((Object[]) captor.getValue());
        assertEquals(10, actualArgs.size());
        assertEquals("CHF", actualArgs.get(9));
    }

    private Matcher<HamcrestSUT> isEquivalent(final int id, final String name) {
        return new BaseMatcher<HamcrestSUT>() {
            @Override
            public boolean matches(final Object item) {
                final HamcrestSUT foo = (HamcrestSUT) item;
                return id == foo.getId() && name.equals(foo.getName());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(String.format("the given object should contain id=%s, name=%s ", id, name));
            }
        };
    }
}