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
package org.semarglproject.sesame.rdf.rdfa;

import org.openrdf.model.ValueFactory;
import org.openrdf.rio.ParseErrorListener;
import org.openrdf.rio.ParseLocationListener;
import org.openrdf.rio.ParserConfig;
import org.openrdf.rio.ParserSetting;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.helpers.BasicParserSettings;
import org.semarglproject.source.StreamProcessor;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.ProcessorGraphHandler;
import org.semarglproject.rdf.rdfa.RdfaParser;
import org.semarglproject.sesame.core.sink.SesameSink;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of Sesame's RDFParser on top of Semargl RDFaParser.
 *
 * @author Peter Ansell p_ansell@yahoo.com
 * @author Lev Khomich levkhomich@gmail.com
 *
 */
public final class SesameRDFaParser implements RDFParser, ProcessorGraphHandler {

    private ParserConfig parserConfig;

    private final StreamProcessor streamProcessor;

    private ParseErrorListener parseErrorListener;

    /**
     * Default constructor. Creates RDFa parser in 1.1 mode with disabled vocabulary expansion feature.
     * <p>
     * Supported settings can be found using {@link #getSupportedSettings()} and can be modified using
     * the {@link ParserConfig} object returned from the {@link #getParserConfig()} method.
     */
    public SesameRDFaParser() {
        setParserConfig(new ParserConfig());
        streamProcessor = new StreamProcessor(RdfaParser.connect(SesameSink.connect(null)));
        streamProcessor.setProperty(StreamProcessor.PROCESSOR_GRAPH_HANDLER_PROPERTY, this);
        // by default this would be set to false if not set here
        setPreserveBNodeIDs(true);
        parseErrorListener = null;
    }

    /**
     * Constructor which allows to specify custom XMLReader.
     * @param xmlReader instance of XMLReader to be used in processing
     */
    public SesameRDFaParser(XMLReader xmlReader) {
        this();
        setXmlReader(xmlReader);
    }

    @Override
    public RDFFormat getRDFFormat() {
        return RDFFormat.RDFA;
    }

    @Override
    public void parse(InputStream in, String baseURI) throws RDFParseException, RDFHandlerException {
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        try {
            parse(reader, baseURI);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    @Override
    public void parse(Reader reader, String baseURI) throws RDFParseException, RDFHandlerException {
        refreshSettings();
        try {
            streamProcessor.process(reader, baseURI);
        } catch (ParseException e) {
            throw new RDFParseException(e);
        }
    }

    @Override
    public void setValueFactory(ValueFactory valueFactory) {
        streamProcessor.setProperty(SesameSink.VALUE_FACTORY_PROPERTY, valueFactory);
    }

    @Override
    public void setRDFHandler(RDFHandler handler) {
        streamProcessor.setProperty(SesameSink.RDF_HANDLER_PROPERTY, handler);
    }

    @Override
    public void setParseErrorListener(ParseErrorListener el) {
        this.parseErrorListener = el;
    }

    @Override
    public void setParseLocationListener(ParseLocationListener ll) {
        // not supported yet
    }

    @Override
    public void setParserConfig(ParserConfig config) {
        this.parserConfig = config;
    }

    @Override
    public ParserConfig getParserConfig() {
        return this.parserConfig;
    }

    @Override
    public Collection<ParserSetting<?>> getSupportedSettings() {
        Collection<ParserSetting<?>> result = new ArrayList<ParserSetting<?>>(5);

        result.add(BasicParserSettings.PRESERVE_BNODE_IDS);
        result.add(SemarglParserSettings.PROCESSOR_GRAPH_ENABLED);
        result.add(SemarglParserSettings.VOCAB_EXPANSION_ENABLED);
        result.add(SemarglParserSettings.RDFA_COMPATIBILITY);
        result.add(SemarglParserSettings.CUSTOM_XML_READER);

        return result;
    }

    @Override
    public void setVerifyData(boolean verifyData) {
        // Does not support verification of data values, see getSupportedSettings for list of supported settings
    }

    @Override
    public void setPreserveBNodeIDs(boolean preserveBNodeIDs) {
        parserConfig.set(BasicParserSettings.PRESERVE_BNODE_IDS, preserveBNodeIDs);
        refreshSettings();
    }

    @Override
    public void setStopAtFirstError(boolean stopAtFirstError) {
        // Does not support changing this setting, see getSupportedSettings for list of supported settings
        // RDFa parser ignores all errors when it is possible to continue
    }

    @Override
    public void setDatatypeHandling(DatatypeHandling datatypeHandling) {
        // Does not support datatype handling, see getSupportedSettings for list of supported settings
    }

    /**
     * Changes {@link RdfaParser#ENABLE_PROCESSOR_GRAPH} setting
     * @param processorGraphEnabled new value to be set
     */
    public void setProcessorGraphEnabled(boolean processorGraphEnabled) {
        parserConfig.set(SemarglParserSettings.PROCESSOR_GRAPH_ENABLED, processorGraphEnabled);
        refreshSettings();
    }

    /**
     * Changes {@link RdfaParser#ENABLE_VOCAB_EXPANSION} setting
     * @param vocabExpansionEnabled new value to be set
     */
    public void setVocabExpansionEnabled(boolean vocabExpansionEnabled) {
        parserConfig.set(SemarglParserSettings.VOCAB_EXPANSION_ENABLED, vocabExpansionEnabled);
        refreshSettings();
    }

    /**
     * Changes {@link RdfaParser#RDFA_VERSION_PROPERTY} setting
     * @param rdfaCompatibility new value to be set
     */
    public void setRdfaCompatibility(short rdfaCompatibility) {
        parserConfig.set(SemarglParserSettings.RDFA_COMPATIBILITY, rdfaCompatibility);
        refreshSettings();
    }

    /**
     * Sets a custom {@link XMLReader}.
     * @param reader new value to be set
     */
    public void setXmlReader(XMLReader reader) {
        parserConfig.set(SemarglParserSettings.CUSTOM_XML_READER, reader);
        refreshSettings();
    }

    /**
     * Refreshes the settings on the stream processor using the current values from the parserConfig.
     */
    private void refreshSettings() {
        streamProcessor.setProperty(RdfaParser.RDFA_VERSION_PROPERTY,
                parserConfig.get(SemarglParserSettings.RDFA_COMPATIBILITY));
        streamProcessor.setProperty(RdfaParser.ENABLE_VOCAB_EXPANSION,
                parserConfig.get(SemarglParserSettings.VOCAB_EXPANSION_ENABLED));
        streamProcessor.setProperty(RdfaParser.ENABLE_PROCESSOR_GRAPH,
                parserConfig.get(SemarglParserSettings.PROCESSOR_GRAPH_ENABLED));
        streamProcessor.setProperty(StreamProcessor.XML_READER_PROPERTY,
                parserConfig.get(SemarglParserSettings.CUSTOM_XML_READER));
    }

    @Override
    public void info(String infoClass, String message) {
    }

    @Override
    public void warning(String warningClass, String message) {
        if (parseErrorListener != null) {
            parseErrorListener.warning(message, -1, -1);
        }
    }

    @Override
    public void error(String errorClass, String message) {
        if (parseErrorListener != null) {
            parseErrorListener.error(message, -1, -1);
        }
    }

}
