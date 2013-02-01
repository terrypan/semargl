package org.semarglproject.xpath;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;


public class XPathAttrFilter extends XMLFilterImpl {
    private String xPath = "/";
    private XMLReader xmlReader;
    private XPathAttrFilter parent;
    private StringBuilder characters = new StringBuilder();
    private Map<String, Integer> elementNameCount = new HashMap<String, Integer>();
   // private String xpathexpr;

    public XPathAttrFilter() {
	super();
    }
    
    public XPathAttrFilter(XMLReader xmlReader) {
	super(xmlReader);
	this.xmlReader = xmlReader;
        //this.setParent(xmlReader);
	
    }

    private XPathAttrFilter(String xPath, XMLReader xmlReader, XPathAttrFilter parent) {
	this(xmlReader);
        this.xPath = xPath;
        this.parent = parent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	
	Integer count = elementNameCount.get(qName);
        if(null == count) {
            count = 1;
        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        String childXPath = xPath + "/" + qName + "[" + count + "]";

        int attsLength = atts.getLength();
        for(int x=0; x<attsLength; x++) {
           // if (atts.getLocalName(x)== "property" || atts.getLocalName(x)== "typeof"){};
            System.out.println("{"+qName+"}" +"-->  "+ childXPath);// + "[@" + atts.getQName(x) + "='" + atts.getValue(x) + ']');
           // xpathexpr = childXPath + "[@" + atts.getQName(x) + "='" + atts.getValue(x) + ']';
        }

        XPathAttrFilter child = new XPathAttrFilter(childXPath, xmlReader, this);
        xmlReader.setContentHandler(child);
        super.startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
	
	//String value = characters.toString().trim();
       // if(value.length() > 0) {
           // System.out.println("XPATH --> "+xPath + "='" + characters.toString() + "'");
       // }
        xmlReader.setContentHandler(parent);
        super.endElement(uri, localName, qName);
    }

//    @Override
//    public void characters(char[] ch, int start, int length) throws SAXException {
//	super.characters(ch, start, length);
//        characters.append(ch, start, length);
//    }
    public String getXPath() {
       return "XPATH";
    }
    
    

}