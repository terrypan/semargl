package org.semarglproject.xpath;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * {@link XMLFilter} that monitors the infoset that flows through it,
 * and provide a capability to {@link #getXPath() compute the XPath to the current element}.
 *
 * <p>
 * In context of JAXB, this can be used to report error location by using XPath.
 * 
 * @author Kohsuke Kawaguchi
 */
public class XPathTrackerFilter extends XMLFilterImpl {

    private final Stack<Histgram> histgrams = new Stack<Histgram>();

    public XPathTrackerFilter() {
        super();
    }

    public XPathTrackerFilter(XMLReader parent) {
        super(parent);
    }

    public XPathTrackerFilter(ContentHandler contentHandler) {
        setContentHandler(contentHandler);
    }

    public void startDocument() throws SAXException {
       // System.out.println("[XPathTrackerFilter] --> StartDocument");
       
        histgrams.clear();
        histgrams.push(new Histgram());
        super.startDocument(); 
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	//System.out.println("[XPathTrackerFilter] --> StartElement: "+localName); 
	histgrams.peek().update(uri,localName,qName, atts);
        histgrams.push(new Histgram());
        //System.out.println("Updating stack with elem: "+ localName);
	 AttributesImpl attributesImpl = new AttributesImpl(atts);
	 attributesImpl.addAttribute("", "xpath", "xpath", "sstring", getXPath());
	 atts = attributesImpl;
	 
        //super.startElement(uri, localName, qName, attributesImpl);
        super.startElement(uri, localName, qName, atts);
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
	//System.out.println("[XPathTrackerFilter] --> EndElement: "+localName); 
	
        histgrams.pop();
        super.endElement(uri, localName, qName);
    }

    /**
     * Gets the XPath to the current element.
     */
    public String getXPath() {
        StringBuilder buf = new StringBuilder();
        for (Histgram h : histgrams) {
            h.appendPath(buf);
        }
     
        //System.out.println("[XPathTrackerFilter] --> getXpath"); 
        return buf.toString();
    }
}
