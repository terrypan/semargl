package org.semarglproject.xpath;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class RunnerAttr {

    public static void main(String[] args) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();

        xr.setContentHandler(new XPathAttrFilter(xr));
        xr.parse(new InputSource(new FileInputStream("C:/terrypan/testRDFaDocs/input/test2.html")));
    }
}