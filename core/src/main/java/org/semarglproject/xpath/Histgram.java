package org.semarglproject.xpath;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;


import java.util.Map;
import java.util.HashMap;

/**
 * Keeps track of the number of siblings element found so far.
 *
 * @author Kohsuke Kawaguchi
 */
final class Histgram {
    private final Map<QName,Integer> occurrence = new HashMap<QName, Integer>();

    private String current;
    private int currentValue;

    public void update(String uri, String localName, String qName, Attributes atts) {
        QName qn = new QName(uri,localName);
        Integer v = occurrence.get(qn);
        if(v==null) v=1;
        else        v+=1 ;
        
        occurrence.put(qn,v);
       // System.out.println("Added: \\"+qn+"["+v+"] in stack ");
        for (int i=0; i< atts.getLength(); i++){
            //System.out.println(i+":  "+atts.getQName(i)+" --> "+atts.getValue(i));
           // if (atts.getQName(i) == "property")
        }
        current = qName;
        currentValue = v;
    }

    public void appendPath(StringBuilder buf) {
        if(current==null){
           // buf.append("HEAD"); // this is the head
            return;
        }
      // System.out.println(current);
       // System.out.println("[XPathTrackerFilter-Histgram] Appending /"+current+"["+currentValue+"]");
        buf.append('/');
        buf.append(current);
        buf.append('[');
        buf.append(currentValue);
        buf.append(']');
    }
}
