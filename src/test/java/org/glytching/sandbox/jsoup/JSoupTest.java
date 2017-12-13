package org.glytching.sandbox.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JSoupTest {

    @Test
    public void canReadScriptElement() {
        String html =
                "<html><script> var uitkformatter = { dependency: ['uitk_localized_dateApi', 'uitk_localized_priceApi', 'uitk_localized_config'] }; </script><script async defer src=\"//www.expedia.com/i18n/28/en_US/JPY/currencyFormats.js?module=exp_currencyformats_JPY\"></script><script> define('exp_currencyformats', [ 'exp_currencyformats_JPY' ], function() { return window.uitkformatter; }); </script><script async defer src=\"//b.travel-assets.com/uitoolkit/2-164/3542359672ff5cd9d827c16bd754bf539fd383b1/core/js/uitk-localize-bundle-min.js\"></script>\n" +
                        "<script language=\"javascript\" type=\"text/javascript\">\n" +
                        "OlAltLang = 'en-us.';\n" +
                        "</script>\n" +
                        "<script type=\"text/javascript\">\n" +
                        "'use strict';\n" +
                        "require('infositeApplication', function(infositeApplication) {\n" +
                        "infositeApplication.start();\n" +
                        "});\n" +
                        "define('infosite/env', function() {\n" +
                        "return {\n" +
                        "isJP: true,\n" +
                        "isVN: false,\n" +
                        "isVSC:false,\n" +
                        "isTD:false\n" +
                        "};\n" +
                        "});\n" +
                        "define('infositeData', [], function() {\n" +
                        "var infosite = {};\n" +
                        "infosite.hotelId = '5522663';\n" +
                        "infosite.guid = '59ad4387-979f-477a-901a-6070f3879ce6';\n" +
                        "infosite.token = '6a06f2f73106c754340f7a459f5d75d588637caa'; </script></html>";

        Document doc = Jsoup.parse(html);

        Elements scriptElements = doc.getElementsByTag("script");

        // the script elements have no identifying charateristic so we must loop
        // until we find the one which contains the "infosite.token" variable
        for (Element element : scriptElements) {
            if (element.data().contains("infosite.token")) {
                // find the line which contains 'infosite.token = <...>;'
                Pattern pattern = Pattern.compile(".*infosite\\.token = ([^;]*);");
                Matcher matcher = pattern.matcher(element.data());
                // we only expect a single match here so there's no need to loop through the matcher's groups
                if (matcher.find()) {
                    System.out.println(matcher.group(1));
                } else {
                    System.err.println("No match found!");
                }
                break;
            }
        }
    }

    @Test
    public void canReadDivByPartialClassName() {
        String html = "<html>" +
                "<span class=\"arial_20 redFont   pid-348-pc\" dir=\"ltr\">-5.60</span>" +
                "<span class=\"arial_20 redFont \" dir=\"ltr\">55.80</span>" +
                "</html>";

        Document doc = Jsoup.parse(html);

        // this will print out -5.60 since the only span with a class matching 'arial_20 redFont   pid-*'
        // is the one with the value: -5.60
        // the other span does not match that CSS selector
        String actual = doc.select("span[class*=\"arial_20 redFont   pid-\"]").text();
        assertThat(actual, is("-5.60"));
    }

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