package org.semarglproject.jena.core.sink;

//import org.semarglproject.rdf.TurtleSerializer;
import org.semarglproject.rdf.rdfa.RdfaParser;
import org.semarglproject.sink.CharOutputSink;
import org.semarglproject.source.StreamProcessor;


public class RunnerPlain {

    public static void main(String[] args) throws Exception {
     
   
	 	//Proxy.setProxy();   
	        
	        // CharOutputSink outputSink = new CharOutputSink();
	        // StreamProcessor sp = new StreamProcessor(RdfaParser.connect(TurtleSerializer.connect(outputSink)));

	        // outputSink.connect(System.out);
	        // sp.process("file:/C:/terrypan/testRDFaDocs/input/testCase-nested.html");
	 	
	 	//TripleSink mySink = new LiteralCounterSink();
	       // StreamProcessor sp = new StreamProcessor(RdfaParser.connect(mySink));

	        // number of literal triples extracted from URI will be printed to System.out
	       // sp.process("file:/C:/terrypan/testRDFaDocs/input/test2.html");
        
    }
     
}


    
