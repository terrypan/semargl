/**
 * Copyright 2012-2013 Lev Khomich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semarglproject.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.semarglproject.rdf.ParseException;
import org.semarglproject.sink.SaxSink;
import org.semarglproject.xpath.XPathTrackerFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

final class SaxSource extends AbstractSource<SaxSink> {

    private XMLReader xmlReader = null;
    private XPathTrackerFilter xpathFilter = null;

    SaxSource(SaxSink sink) {
        super(sink);
    }

    @Override
    public void process(Reader reader, String mimeType, String baseUri) throws ParseException {
        try {
            initXmlReader();
        } catch (SAXException e) {
            throw new ParseException("Can not instantiate XMLReader", e);
        }
        try {
            sink.setBaseUri(baseUri);
           
            xpathFilter = new XPathTrackerFilter();
            xpathFilter.setParent(xmlReader);
            xpathFilter.setContentHandler(sink);
            //xmlReader.parse(new InputSource(reader));
            xpathFilter.parse(new InputSource(reader));
        } catch (SAXException e) {
            ParseException wrappedException = sink.processException(e);
            try {
                sink.endDocument();
            } catch (SAXException e2) {
                // do nothing
            }
            throw wrappedException;
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }


    @Override
    public void process(InputStream inputStream, String mimeType, String baseUri) throws ParseException {
        Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        try {
            process(reader, mimeType, baseUri);
        } finally {
            BaseStreamProcessor.closeQuietly(reader);
        }
    }

    private void initXmlReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        }
        xmlReader.setContentHandler(sink);
        xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", sink);    
    }

    public void setXmlReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

}
