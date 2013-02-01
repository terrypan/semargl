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
package org.semarglproject.jena.rdf.rdfa;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.impl.RDFReaderFImpl;
import org.semarglproject.jena.core.sink.JenaSink;
import org.semarglproject.source.StreamProcessor;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.RdfaParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Implementation or Jena's RDFReader on top of Semargl APIs.
 */
public final class JenaRdfaReader implements RDFReader {

    private final StreamProcessor streamProcessor;

    /**
     * Default constructor. Creates RDFa parser in 1.1 mode with disabled vocabulary expansion feature.
     * Properties can be changed using {@link #setProperty(String, Object)} calls with property keys
     * from {@link RdfaParser}.
     */
    public JenaRdfaReader() {
        streamProcessor = new StreamProcessor(RdfaParser.connect(JenaSink.connect(null)));
    }

    
    /**
     * Injects information about Semargl RDFa parser to Jena framework.
     */
    public static void inject() {
        RDFReaderFImpl.setBaseReaderClassName("RDFA", JenaRdfaReader.class.getName());
    }

    @Override
    public void read(Model model, Reader r, String base) {
        streamProcessor.setProperty(JenaSink.OUTPUT_MODEL_PROPERTY, model);
        try {
            streamProcessor.process(r, base);
        } catch (ParseException e) {
            // do nothing
        }
    }

    @Override
    public void read(Model model, InputStream r, String base) {
        InputStreamReader reader = new InputStreamReader(r);
        try {
            model.begin();
        read(model, reader, base);
        } finally {
            model.abort();
            try {
                reader.close();
            } catch (IOException e) {
                // do nothing
            }
        }
        model.commit();
    }

    @Override
    public void read(Model model, String url) {
        try {
            URL uri = new URL(url);
            read(model, uri.openStream(), url);
        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    public Object setProperty(String propName, Object propValue) {
        return streamProcessor.setProperty(propName, propValue);
    }

    @Override
    public RDFErrorHandler setErrorHandler(RDFErrorHandler errHandler) {
        return null;
    }
}
