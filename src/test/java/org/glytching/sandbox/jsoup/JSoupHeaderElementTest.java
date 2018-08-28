package org.glytching.sandbox.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JSoupHeaderElementTest {

    @Test
    public void headerElementsWhichAreInOrderAreValid() {
        String html = "<html><body><h1></h1><h2></h2><h3></h3><h4></h4><h5></h5><h6></h6></body></html>";

        validateHeaderElementOrdering(html);
    }

    @Test
    public void headerElementsWhichAreNotInOrderAreInvalid() {
        String html = "<html><body><h1></h1><h3></h3><h2></h2><h4></h4><h5></h5><h6></h6></body></html>";

        try {
            validateHeaderElementOrdering(html);
            fail("Expected out of order header elements to be deemed invalid!");
        } catch (RuntimeException ex) {
            assertEquals("Header tags are out of order: h3 came after h1", ex.getMessage());
        }
    }

    @Test
    public void headerElementsWhichAreNotCompleteAndInOrderAreInvalid() {
        String html = "<html><body><h2></h2><h4></h4><h5></h5></body></html>";

        try {
            validateHeaderElementOrdering(html);
            fail("Expected out of order header elements to be deemed invalid!");
        } catch (RuntimeException ex) {
            assertEquals("Header tags are out of order: h4 came after h2", ex.getMessage());
        }
    }

    @Test
    public void willValidateEvenWhenThereIsASingleHeaderElement() {
        String html = "<html><body><h3></h3></body></html>";

        try {
            validateHeaderElementOrdering(html);
            fail("Expected out of order header elements to be deemed invalid!");
        } catch (RuntimeException ex) {
            assertEquals("Header tags are out of order, there's only one header tag and it is not h1!", ex.getMessage());
        }
    }

    // this fails fast i.e. as soon as it find an out-of-order header element, if you are interested in gathering and
    // reporting all out-of-order header elements then you'll want to gather the failures in a collection and then
    // include the failures in an exception which is thrown **after** you have iterated over all header tag
    public void validateHeaderElementOrdering(String html) {
        Document document = Jsoup.parse(html);

        Elements headerElements = document.select("h1, h2, h3, h4, h5, h6");

        List<String> headerTags = new ArrayList<>();
        for (Element element : headerElements) {
            headerTags.add(element.nodeName());
        }

        // if there is only one entry then it must be h1 because (a) every header must be preceded by its parent and
        // (b) if there is only one header present then this header is not preceded by anything and (c) only h1 has
        // no parent
        if (headerTags.size() == 1) {
            String currentTag = headerTags.get(0);
            long currentTagPosition = Long.valueOf(currentTag.substring(1, 2));

            if (currentTagPosition != 1) {
                throw new RuntimeException("Header tags are out of order, there's only one header tag and it is not h1!");
            }
        }

        // now walk the headerTags and for each entry insist that it is preceded by the immediately prior header,
        // based on the ordering h6 -> h5 -> h4 -> h3 -> h2 -> h1
        for (int i = 1; i < headerTags.size(); i++) {
            String previousTag = headerTags.get(i - 1);
            String currentTag = headerTags.get(i);

            long currentTagPosition = Long.valueOf(currentTag.substring(1, 2));
            long previousTagTagPosition = Long.valueOf(previousTag.substring(1, 2));

            if (currentTagPosition != (previousTagTagPosition + 1)) {
                throw new RuntimeException(String.format("Header tags are out of order: %s came after %s", currentTag, previousTag));
            }
        }
    }
}