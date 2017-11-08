package org.glytching.sandbox.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JSoupTest {

    @Test
    public void canReadHtml() {
        String html = "<html><head><title>First parse</title></head><body><p>Parsed HTML into a doc.</p></body></html>";

        Document doc = Jsoup.parse(html);

        Elements ps = doc.getElementsByTag("p");


        assertThat(ps.size(), is(1));
        assertThat(ps.get(0).text(), is("Parsed HTML into a doc."));
    }

    @Test
    public void canReadHtmlByNestedDivClass() {
        String html = "<html><head><title>First parse</title></head><body><div class=\"classA\">" +
                "<div class=\"nestedClassA\">Inside nestedClassA</div></div>" +
                "<div class=\"classB\">Inside classB</div></body></html>";

        Document doc = Jsoup.parse(html);

        Elements elements = doc.select("div.classA > div.nestedClassA");

        assertThat(elements.size(), is(1));
        assertThat(elements.get(0).text(), is("Inside nestedClassA"));
    }
}