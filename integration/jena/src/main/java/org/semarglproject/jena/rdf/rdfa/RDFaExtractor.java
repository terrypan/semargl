/**
 * Copyright 2012-2013 Terry Panagoulis
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

import org.semarglproject.jena.core.sink.JenaSink;
import org.semarglproject.jena.core.sink.ReifyXpathJenaSink;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.RdfaParser;
import org.semarglproject.rdf.rdfa.RdfaXPathParser;
import org.semarglproject.source.StreamProcessor;
import org.xml.sax.SAXParseException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * RDFaExtractor on top of Semargl API.
 * 
 * 
 */
public class RDFaExtractor {

    private  Model model;
    
    /**
     * Default constructor.
     * 
     * 
     */
    public RDFaExtractor() {
        model = ModelFactory.createDefaultModel();
    }

    /**
     * Callback method for extracting triples from RDFa into a Jena model.
     * @param uri The document URI (if local add *file:/* before document location )
     */
    public Model extractRDFa(String uri){
       // long time = System.currentTimeMillis();
        
        StreamProcessor sp = new StreamProcessor(RdfaParser.connect(JenaSink.connect(model)));
       // sp.setProperty(RdfaParser.ENABLE_PROCESSOR_GRAPH, true);
        try {
            sp.process(uri);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return model;
        
    }
    
    /**
     * Callback method for extracting reification Statements alongside their Xpath location in the document into a Jena model.
     * @param uri The document URI (if local add <b>file:/</b> before document location )
     */
    public Model extractReifiedXpathRDFa(String uri) {
        StreamProcessor sp = new StreamProcessor(RdfaXPathParser.connect(ReifyXpathJenaSink.connect(model)));
        try {
            sp.process(uri);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return model;
        
    }
    
    /**
     * Callback method for transforming triples in Jena model as Reification Statements.
     * @param uri The document URI (if local add *file:/* before document location )
     */
    public Model getReificationModel(Model model){
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()){
            Statement tempstmt = iter.next();
            model.createReifiedStatement(tempstmt);
            iter.remove();
        }
        return model;
    }
    
}
