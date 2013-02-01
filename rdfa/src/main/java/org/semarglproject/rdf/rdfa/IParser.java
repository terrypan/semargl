package org.semarglproject.rdf.rdfa;

import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.ProcessorGraphHandler;
import org.semarglproject.vocab.RDFa;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public interface IParser {

    /**
     * Used as a key with {@link #setProperty(String, Object)} method.
     * RDFa version compatibility. Allowed values are {@link RDFa#VERSION_10} and {@link RDFa#VERSION_11}.
     */
    public static final String RDFA_VERSION_PROPERTY = "http://semarglproject.org/rdfa/properties/version";
    /**
     * Used as a key with {@link #setProperty(String, Object)} method.
     * Allows to specify handler for <a href="http://www.w3.org/ns/rdfa.html">processor graph</a> events.
     * Subclass of {@link ProcessorGraphHandler} must be passed as a value.
     */
    public static final String PROCESSOR_GRAPH_HANDLER_PROPERTY = "http://semarglproject.org/rdfa/properties/processor-graph-handler";
    /**
     * Used as a key with {@link #setProperty(String, Object)} method.
     * Enables or disables generation of triples from output graph.
     */
    public static final String ENABLE_OUTPUT_GRAPH = "http://semarglproject.org/rdfa/properties/enable-output-graph";
    /**
     * Used as a key with {@link #setProperty(String, Object)} method.
     * Enables or disables generation of triples from processor graph.
     * ProcessorGraphHandler will receive events regardless of this option.
     */
    public static final String ENABLE_PROCESSOR_GRAPH = "http://semarglproject.org/rdfa/properties/enable-processor-graph";
    /**
     * Used as a key with {@link #setProperty(String, Object)} method.
     * Enables or disables <a href="http://www.w3.org/TR/2012/REC-rdfa-core-20120607/#s_vocab_expansion">vocabulary
     * expansion</a> feature.
     */
    public static final String ENABLE_VOCAB_EXPANSION = "http://semarglproject.org/rdfa/properties/enable-vocab-expansion";

    void startDocument();

    void endDocument() throws SAXException;

    void startElement(String nsUri, String localName, String qName,
            Attributes attrs) throws SAXException;

    void endElement(String nsUri, String localName, String qName)
            throws SAXException;

    void characters(char[] buffer, int start, int length) throws SAXException;

    void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException;

    void startDTD(String name, String publicId, String systemId)
            throws SAXException;

    void startPrefixMapping(String prefix, String uri) throws SAXException;

    void endPrefixMapping(String prefix) throws SAXException;

    boolean setPropertyInternal(String key, Object value);

    void setBaseUri(String baseUri);

    void info(String infoClass, String message);

    void warning(String warningClass, String message);

    void error(String errorClass, String message);

    ParseException processException(SAXException e);

    void addNonLiteral(String subj, String pred, String obj);

    void addPlainLiteral(String subj, String pred, String content, String lang);

    void addTypedLiteral(String subj, String pred, String content, String type);
    
    void addNonLiteral(String subj, String pred, String obj, String xpath);

    void addPlainLiteral(String subj, String pred, String content, String lang, String xpath);

    void addTypedLiteral(String subj, String pred, String content, String type, String xpath);

    void setDocumentLocator(Locator locator);

    void processingInstruction(String target, String data) throws SAXException;

    void skippedEntity(String name) throws SAXException;

    void startEntity(String s) throws SAXException;

    void endEntity(String s) throws SAXException;

    void startCDATA() throws SAXException;

    void endCDATA() throws SAXException;

    void comment(char[] chars, int i, int i1) throws SAXException;

    void endDTD() throws SAXException;

    Vocabulary loadVocabulary(String vocabUrl);

}