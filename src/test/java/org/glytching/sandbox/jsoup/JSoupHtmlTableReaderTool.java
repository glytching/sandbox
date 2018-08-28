package org.glytching.sandbox.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JSoupHtmlTableReaderTool {
    private static final Logger logger = LoggerFactory.getLogger(JSoupHtmlTableReaderTool.class);
    private static String HTML =
            "<html><table class=\"textfont\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\" border=\"0\">\n" +
                    "    <tbody>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Batch ID</td><td class=\"ctext\">d32654464bdb424396f6a91f2af29ecf</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">ALM Server</td>\n" +
                    "            <td class=\"ctext\"></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">ALM Domain/Project</td>\n" +
                    "            <td class=\"ctext\">EBUSINESS/STERLING</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">TestSet URL</td>\n" +
                    "            <td class=\"ctext\">almtestset://<a href=\"http://localhost.com\">localhost</a></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Tests Executed</td>\n" +
                    "            <td class=\"ctext\"><b>6</b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Start Time</td>\n" +
                    "            <td class=\"ctext\">08/31/2017 12:20:46 PM</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Finish Time</td>\n" +
                    "            <td class=\"ctext\">08/31/2017 02:31:46 PM</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Total Duration</td>\n" +
                    "            <td class=\"ctext\"><b>2h 11m </b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Test Parameters</td>\n" +
                    "            <td class=\"ctext\"><b>{&quot;browser&quot;:&quot;chrome&quot;,&quot;browser-version&quot;:&quot;56&quot;,&quot;language&quot;:&quot;english&quot;,&quot;country&quot;:&quot;US&quot;}</b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Passed</td>\n" +
                    "            <td class=\"ctext\" style=\"color:#269900\"><b>0</b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Failed</td>\n" +
                    "            <td class=\"ctext\" style=\"color:#990000\"><b>6</b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Not Completed</td>\n" +
                    "            <td class=\"ctext\" style=\"color: ##ff8000;\"><b>0</b></td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td class=\"chl\" width=\"20%\">Test Pass %</td>\n" +
                    "            <td class=\"ctext\" style=\"color:#990000;font-size:14px\"><b>0.0%</b></td>\n" +
                    "        </tr>\n" +
                    "    </tbody></table></html>";

    @Test
    public void parseTable() {
        Document doc = Jsoup.parse(HTML);

        // declare a holder to contain the 'mapped rows', this is a map based on the assumption that every row represents a discreet key:value pair
        Map<String, String> asMap = new HashMap<>();
        Element table = getTable(doc);

        // now walk though the rows creating a map for each one
        Elements rows = table.select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            // expecting this table to consist of key:value pairs where the first cell is the key and the second cell is the value
            if (cols.size() == 2) {
                asMap.put(cols.get(0).text(), cols.get(1).text());
            } else {
                throw new RuntimeException(String.format("Cannot parse the table row: %s to a key:value pair because it contains %s cells!", row.text(), cols.size()));
            }
        }

        logger.info(asMap.toString());
    }

    private Element getTable(Document doc) {
        Elements tables = doc.select("table");
        for (int i = 0; i < tables.size(); i++) {
            // this xpath //td[text() = 'TestSet URL']/ancestor::table[1] will find the first table which contains the
            // text "TestSet URL" anywhere in its body
            // this crude evaluation is the JSoup equivalent of that xpath
            if (tables.get(i).text().contains("TestSet URL")) {
                return tables.get(i);
            }
        }
        throw new RuntimeException("Cannot find a table element which contains 'TestSet URL'!");
    }
}