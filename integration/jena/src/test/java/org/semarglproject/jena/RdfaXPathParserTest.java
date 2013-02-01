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
package org.semarglproject.jena;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.semarglproject.jena.core.sink.ReifyXpathJenaSink;
import org.semarglproject.source.StreamProcessor;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.IParser;
import org.semarglproject.rdf.rdfa.RdfaTestBundle;
import org.semarglproject.rdf.rdfa.RdfaTestBundle.TestCase;
import org.semarglproject.rdf.rdfa.RdfaXPathParser;
import org.semarglproject.vocab.RDFa;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import static org.semarglproject.rdf.rdfa.RdfaTestBundle.SaveToFileCallback;
import static org.semarglproject.rdf.rdfa.RdfaTestBundle.runTestBundle;

public final class RdfaXPathParserTest {

    private Model model;
    private StreamProcessor streamProcessor;
    private SaveToFileCallback jenaCallback = new SaveToFileCallback() {
        @Override
        public void run(Reader input, String inputUri, Writer output, short rdfaVersion) throws ParseException {
            streamProcessor.setProperty(IParser.RDFA_VERSION_PROPERTY, rdfaVersion);
            try {
                streamProcessor.process(input, inputUri);
            } finally {
                model.write(output, "TURTLE");
            }
        }
    };

    @BeforeClass
    public void init() throws SAXException {
        RdfaTestBundle.prepareTestDir();
        model = ModelFactory.createDefaultModel();

        streamProcessor = new StreamProcessor(RdfaXPathParser.connect(ReifyXpathJenaSink.connect(model)));
        streamProcessor.setProperty(IParser.ENABLE_VOCAB_EXPANSION, true);
    }

    @BeforeMethod
    public void setUp() {
        model.removeAll();
    }

    @DataProvider
    public static Object[][] getRdfa10Xhtml1TestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.0", "xhtml1"));
    }

    @DataProvider
    public static Object[][] getRdfa10SvgTestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.0", "svg"));
    }

    @DataProvider
    public static Object[][] getRdfa11Html4TestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.1", "html4"));
    }

    @DataProvider
    public static Object[][] getRdfa11Xhtml1TestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.1", "xhtml1"));
    }

    @DataProvider
    public static Object[][] getRdfa11Html5TestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.1", "html5"));
    }

    @DataProvider
    public static Object[][] getRdfa11XmlTestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.1", "xml"));
    }

    @DataProvider
    public static Object[][] getRdfa11SvgTestSuite() {
        return convertToDataProvider(RdfaTestBundle.getTestCases("rdfa1.1", "svg"));
    }

    private static Object[][] convertToDataProvider(Collection<TestCase> tests) {
        Object[][] result = new Object[tests.size()][];
        int i = 0;
        for (TestCase testCase : tests) {
            result[i++] = new Object[]{testCase};
        }
        return result;
    }

    @Test(dataProvider = "getRdfa10Xhtml1TestSuite")
    public void Rdfa10Xhtml1TestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_10);
    }

    @Test(dataProvider = "getRdfa10SvgTestSuite")
    public void Rdfa10SvgTestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_10);
    }

    @Test(dataProvider = "getRdfa11Html4TestSuite")
    public void Rdfa11Html4TestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11Xhtml1TestSuite")
    public void Rdfa11Xhtml1TestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11Html5TestSuite")
    public void Rdfa11Html5TestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11XmlTestSuite")
    public void Rdfa11XmlTestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11SvgTestSuite")
    public void Rdfa11SvgTestsJena(TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

}
