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

import static org.semarglproject.rdf.rdfa.RdfaTestBundle.runTestBundle;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import org.semarglproject.jena.rdf.rdfa.JenaRdfaXPathReader;
import org.semarglproject.rdf.ParseException;
import org.semarglproject.rdf.rdfa.RdfaTestBundle;
import org.semarglproject.rdf.rdfa.RdfaTestBundle.SaveToFileCallback;
import org.semarglproject.vocab.RDFa;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaRdfaXPathReaderTest {

    private Model model;
    private SaveToFileCallback jenaCallback = new SaveToFileCallback() {
        @Override
        public void run(Reader input, String inputUri, Writer output, short rdfaVersion) throws ParseException {
            try {
                model.read(input, inputUri, "RDFA-XPATH");
            } finally {
                model.write(output, "TURTLE");
            }
        }
    };

    @BeforeClass
    public void init() throws SAXException, ClassNotFoundException {
        RdfaTestBundle.prepareTestDir();
        model = ModelFactory.createDefaultModel();
        JenaRdfaXPathReader.inject();
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

    private static Object[][] convertToDataProvider(Collection<RdfaTestBundle.TestCase> tests) {
        Object[][] result = new Object[tests.size()][];
        int i = 0;
        for (RdfaTestBundle.TestCase testCase : tests) {
            result[i++] = new Object[]{testCase};
        }
        return result;
    }

    @Test(dataProvider = "getRdfa10Xhtml1TestSuite")
    public void Rdfa10Xhtml1TestsJena(RdfaTestBundle.TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_10);
    }

    @Test(dataProvider = "getRdfa10SvgTestSuite")
    public void Rdfa10SvgTestsJena(RdfaTestBundle.TestCase testCase) {
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_10);
    }

    @Test(dataProvider = "getRdfa11Html4TestSuite")
    public void Rdfa11Html4TestsJena(RdfaTestBundle.TestCase testCase) {
        // vocabulary expansion is disabled by default
        if (testCase.getInput().matches(".+024[0-5].+")) {
            return;
        }
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11Xhtml1TestSuite")
    public void Rdfa11Xhtml1TestsJena(RdfaTestBundle.TestCase testCase) {
        // vocabulary expansion is disabled by default
        if (testCase.getInput().matches(".+024[0-5].+")) {
            return;
        }
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11Html5TestSuite")
    public void Rdfa11Html5TestsJena(RdfaTestBundle.TestCase testCase) {
        // vocabulary expansion is disabled by default
        if (testCase.getInput().matches(".+024[0-5].+")) {
            return;
        }
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11XmlTestSuite")
    public void Rdfa11XmlTestsJena(RdfaTestBundle.TestCase testCase) {
        // vocabulary expansion is disabled by default
        if (testCase.getInput().matches(".+024[0-5].+")) {
            return;
        }
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

    @Test(dataProvider = "getRdfa11SvgTestSuite")
    public void Rdfa11SvgTestsJena(RdfaTestBundle.TestCase testCase) {
        // vocabulary expansion is disabled by default
        if (testCase.getInput().matches(".+024[0-5].+")) {
            return;
        }
        runTestBundle(testCase, jenaCallback, RDFa.VERSION_11);
    }

}
