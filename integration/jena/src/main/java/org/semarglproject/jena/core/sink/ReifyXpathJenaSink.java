package org.semarglproject.jena.core.sink;

import org.semarglproject.rdf.ParseException;
import org.semarglproject.sink.TripleSink;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;


/**
 * Implementation if {@link TripleSink} which feeds triples from Semargl's pipeline to Jena's {@link Model}.
 * <p>
 *     List of supported options:
 *     <ul>
 *         <li>{@link #OUTPUT_MODEL_PROPERTY}</li>
 *     </ul>
 * </p>
 */
public final class ReifyXpathJenaSink extends AbstractJenaSink {

    private static final int DEFAULT_BATCH_SIZE = 512;

    private final int batchSize;

    private Statement[] statements;
    private int statementsSize;
    
    private Property locationProp;
    private Literal xpathstr;

    private ReifyXpathJenaSink(Model model, int batchSize) {
        super(model);
        this.batchSize = batchSize;
    }
    /**
     * Instantiates sink for specified Jena {@link Model}
     * @param model model to sink triples to
     * @return new instance of Jena sink
     */
    public static TripleSink connect(Model model) {
        return new ReifyXpathJenaSink(model, DEFAULT_BATCH_SIZE);
    }

    private void newBatch() {
        statements = new Statement[batchSize];
        statementsSize = 0;
    }

    @Override
    protected void addTriple(Node subj, Node pred, Node obj) {
        
    }

//    private ReifiedStatement tripletoRS(Node subj, Node pred, Node obj) {
//        
//        Triple temptriple = new Triple(subj, pred, obj);
//        Statement stmt = model.asStatement(temptriple);
//        ReifiedStatement rStmt= model.createReifiedStatement(stmt);
//        locationProp= model.createProperty("http://www.example.org/objectPath");
//        Literal xpathstr= model.createLiteral("xpath");
//        
//        return rStmt;
//    }
    
    @Override
    public void startStream() throws ParseException {
        newBatch();
    }

    @Override
    public void endStream() throws ParseException {
        if (statementsSize == 0) {
            return;
        }
        Statement[] dummy = new Statement[statementsSize];
        System.arraycopy(statements, 0, dummy, 0, statementsSize);
        model.enterCriticalSection(Lock.WRITE);
        try {
        model.add(dummy);
        } finally {
        model.leaveCriticalSection();
        }
    }

    @Override
    public void setBaseUri(String baseUri) {
    }
   
    @Override
    protected void addTriple(Node subj, Node pred, Node obj, String xpath) {
        
        Triple temptriple = new Triple(subj, pred, obj);
        Statement stmt = model.asStatement(temptriple);
      
        ReifiedStatement rStmt= stmt.createReifiedStatement();
        locationProp= model.createProperty("http://www.example.org/objectPath");
        xpathstr = (xpath != null)? model.createLiteral(xpath): model.createLiteral("Not available");
        
        Statement rsStatement = ResourceFactory.createStatement(rStmt, locationProp, xpathstr);
        
        statements[statementsSize++] = rsStatement; 
        
        
        if (statementsSize == batchSize) {
            model.enterCriticalSection(Lock.WRITE);
            try {
            model.add(statements);
            } finally {
            model.leaveCriticalSection();
            }
            newBatch();
        }
    }
    
}
