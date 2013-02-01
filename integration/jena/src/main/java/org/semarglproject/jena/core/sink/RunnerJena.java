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
package org.semarglproject.jena.core.sink;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.semarglproject.jena.rdf.rdfa.JenaRdfaReader;
import org.semarglproject.jena.rdf.rdfa.JenaRdfaXPathReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RunnerJena {

    public static void main(String[] args) throws Exception {
     
	Model model = ModelFactory.createDefaultModel();
        Proxy.setProxy();   

	JenaRdfaXPathReader.inject();
	URL rdfaDoc = new URL("http://rdfa.info/test-suite/test-cases/rdfa1.1/html5/0099.html");
	BufferedReader in = new BufferedReader(new InputStreamReader(rdfaDoc.openStream()));
	model.read(in, "", "RDFA-XPATH");
	model.write(System.out, "TTL");
	System.out.println("Model size: " + model.size());
        
    }
     
}


    
