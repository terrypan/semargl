package org.semarglproject.xpath;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XPathTrackerImpl extends XPathTrackerFilter {

    public void startElement (String namespaceUri, String localName,
            String qualifiedName, Attributes attributes)
         throws SAXException{
	//super.startElement(namespaceUri, localName, qualifiedName, attributes);
	    System.out.println("[XPathTrackerImpl] --> StartElement: "+ localName);
            AttributesImpl attributesImpl = new AttributesImpl(attributes);
            //System.out.println(getXPath());
            attributesImpl.addAttribute("", "xpath", "xpath", "sstring", getXPath());
            System.out.println("[XPathTrackerImpl] --> Received XPath for : "+ localName+" --> "+getXPath());
            attributes = attributesImpl;
        super.startElement(namespaceUri, localName, qualifiedName, attributes);     
}
    public void endElement(String uri, String localName, String qName) throws SAXException {
   	System.out.println("[XPathTrackerImpl] --> EndElement: "+localName); 
   	
         
           super.endElement(uri, localName, qName);
       }
    
}
