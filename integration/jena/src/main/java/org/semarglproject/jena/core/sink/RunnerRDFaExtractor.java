package org.semarglproject.jena.core.sink;

import org.semarglproject.jena.rdf.rdfa.RDFaExtractor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RunnerRDFaExtractor {

    public static void main(String[] args)  {
     
        RDFaExtractor extractor = new RDFaExtractor();
        Model model = ModelFactory.createDefaultModel();
        Proxy.setProxy();   
       
        //model = extractor.extractRDFa("file:/C:/terrypan/testRDFaDocs/input/testCase-nested.html");
        model = extractor.extractRDFa("http://rdfa.info/test-suite/test-cases/rdfa1.1/html5/0099.html");
        // model = extractor.extractRDFa("file:/C:/terrypan/testRDFaDocs/input/test2.html");
       // model = extractor.getReificationModel(model);
      // model = extractor.extractReifiedXpathRDFa("file:/C:/terrypan/testRDFaDocs/input/test2.html");
        
        
      //model = extractor.extractRDFa("http://rdfa.info/test-suite/test-cases/rdfa1.0/xhtml1/0212.xhtml");
      //model = extractor.extractRDFa("http://rdfa.info/test-suite/test-cases/rdfa1.1/xhtml1/0198.xhtml");
      //model.write(System.out, "Turtle");

        model.write(System.out, "TTL");
	System.out.println("Model size: " + model.size());
        
    }
     
}


    
