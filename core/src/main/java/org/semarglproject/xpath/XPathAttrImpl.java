package org.semarglproject.xpath;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;


public class XPathAttrImpl extends XPathAttrFilter{
    
    public XPathAttrImpl(XMLReader xmlReader) {
	super(xmlReader);
    }

    public void startElement (String namespaceUri, String localName,
            String qualifiedName, Attributes attributes)
         throws SAXException{

	AttributesImpl attributesImpl = new AttributesImpl(attributes);
        //System.out.println(getXPath());
        attributesImpl.addAttribute("", "xpath", "xpath", "sstring", getXPath());
        attributes = attributesImpl;
        super.startElement(namespaceUri, localName, qualifiedName, attributes);
}
    
}