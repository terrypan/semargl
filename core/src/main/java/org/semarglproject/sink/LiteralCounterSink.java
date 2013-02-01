package org.semarglproject.sink;

import org.semarglproject.rdf.ParseException;

public class LiteralCounterSink implements TripleSink {

    private int count = 0;

    public void startStream() throws ParseException {
        count = 0;
    }

    public void addNonLiteral(String subj, String pred, String obj) {
        // ignore non literal
    }

    public void addPlainLiteral(String subj, String pred, String content, String lang) {
        count++;
    }

    public void addTypedLiteral(String subj, String pred, String content, String type) {
        count++;
    }

    public void endStream() throws ParseException {
        System.out.println("Triples processed: " + count);
    }

    // ignored callbacks
    public void setBaseUri(String baseUri) {}
    public boolean setProperty(String key, Object value) {
	return false;}

    @Override
    public void addNonLiteral(String subj, String pred, String obj, String xpath) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void addPlainLiteral(String subj, String pred, String content,
	    String lang, String xpath) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void addTypedLiteral(String subj, String pred, String content,
	    String type, String xpath) {
	// TODO Auto-generated method stub
	
    }
}