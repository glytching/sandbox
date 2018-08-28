package org.glytching.sandbox.xml;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlReaderTest {
    private static final Logger logger = LoggerFactory.getLogger(XmlReaderTest.class);

    @Test
    public void canReadXml() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<Info><document><document>1234</document></document></Info>";

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));

        NodeList errNodes = doc.getElementsByTagName("error");
        if (errNodes.getLength() > 0) {
            Element err = (Element) errNodes.item(0);
        } else {
            Node value = doc.getElementsByTagName("document").item(1);

            for (int i = 0; i < doc.getElementsByTagName("document").getLength(); i++) {
                logger.info(doc.getElementsByTagName("document").item(i).getTextContent());
            }

            NodeList childNodes = doc.getElementsByTagName("document").item(0).getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                logger.info(item.getTextContent());
            }
            logger.info("...");
            logger.info(doc.getElementsByTagName("document").item(1).getTextContent());
            logger.info(value.toString());
        }
    }

}
