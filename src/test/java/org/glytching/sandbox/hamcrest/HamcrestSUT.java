package org.glytching.sandbox.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.List;
import java.util.Locale;

public class HamcrestSUT {

    private final int id;
    private final String name;
    private final Double price;

    public HamcrestSUT(int id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void getMessage(String code, Object[] args, Locale locale) {

    }

    public static class CustomMatcher extends BaseMatcher<List<HamcrestSUT>> {
        public String name;
        public int id;

        private CustomMatcher(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public static CustomMatcher matches(String name, int id) {
            return new CustomMatcher(name, id);
        }

        @Override
        public boolean matches(Object item) {
            HamcrestSUT incoming = (HamcrestSUT) item;
            return incoming.getName().equals(name) && incoming.getId() == id;
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}