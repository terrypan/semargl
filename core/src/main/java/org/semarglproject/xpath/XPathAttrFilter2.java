package org.semarglproject.xpath;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;


public class XPathAttrFilter2 extends XMLFilterImpl {
    private String xPath = "/";
    private XMLReader xmlReader;
    private XPathAttrFilter2 parent;
    private StringBuilder characters = new StringBuilder();
    private Map<String, Integer> elementNameCount = new HashMap<String, Integer>();
    private String xpathExpr;
    
    public XPathAttrFilter2(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
       // this.setParent(xmlReader);
    }

    private XPathAttrFilter2(String xPath, XMLReader xmlReader, XPathAttrFilter2 parent) {
        this(xmlReader);
        this.xPath = xPath;
        this.parent = parent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	super.startElement(uri, localName, qName, atts);
	Integer count = elementNameCount.get(qName);
        if(null == count) {
            count = 1;
        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        String childXPath = xPath + "/" + qName + "[" + count + "]";

        //int attsLength = atts.getLength();
        //for(int x=0; x<attsLength; x++) {
           // System.out.println("Element: "+ qName+" --> "+ childXPath + "[@" + atts.getQName(0) + "='" + atts.getValue(0) + ']');
       // }
            xpathExpr = childXPath + "[@" + atts.getQName(0) + "='" + atts.getValue(0) + ']';

        XPathAttrFilter2 child = new XPathAttrFilter2(childXPath, xmlReader, this);
        xmlReader.setContentHandler(child);
        System.out.println("Start"+ localName+": "+xPath );
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
	super.endElement(uri, localName, qName);
        String value = characters.toString().trim();
       // if(value.length() > 0) {
        System.out.println("End"+ localName+": "+xPath );
        	    //+ "='" + characters.toString() + "'");
       // }
        xmlReader.setContentHandler(parent);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }

    /**
     * Gets the XPath to the current element.
     */
    public String getXPath() {
      return xpathExpr;
    }
}