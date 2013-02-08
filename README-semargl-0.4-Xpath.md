# RDFaExtractor usage #

**RDFaExtractor** is the main class responsible for extracting RDFa from documents.  
Methods exposed implement different parsing options:

- **extractRDFa(String url)** 

The method returns a Jena model populated with RDF triples extracted from the given url. 

e.g. 

	<http://www.gazettes.co.uk/id/notice/1435406#EstateAdministrator>  a 
	<http://xmlns.com/foaf/0.1/Agent>


- **getReificationModel(Model model)**

This helper method returns a Jena model containing the reified version of the model's statements. 

e.g. 

	[]   a   <http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement> ;
	<http://www.w3.org/1999/02/22-rdf-syntax-ns#object>
             <http://xmlns.com/foaf/0.1/Agent> ;
    <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate>
         	 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ;
    <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject>
         	 <http://www.gazettes.co.uk/id/notice/1435406#EstateAdministrator> .


- **extractReifiedXpathRDFa(String url)**

The method returns a Jena model populated with reified statements (based on the RDF triples) extracted from the url, alongside a property indicating the XPath expression that exposes the document location where the triple was identified in.
 
e.g. 

	[]   a   <http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement> ;
	<http://www.w3.org/1999/02/22-rdf-syntax-ns#object>
             <http://xmlns.com/foaf/0.1/Agent> ;
    <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate>
         	 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ;
    <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject>
         	 <http://www.gazettes.co.uk/id/notice/1435406#EstateAdministrator> .
    <http://www.example.org/objectPath>
              "/html[1]/body[1]/article[1]/div[1]/section[1]/span[10]" .

## RDFa parser implementation ##

RDFaExtractor uses [Semargl](http://semarglproject.org/) as its underlying RDFa 1.1 parser engine. 

Since the official release does not yet support XPath tracking, an custom extended fork was implemented as [Semargl-0.4-Xpath](https://github.com/terrypan/semargl/tree/semargl-0.4-Xpath) to handle XPath expressions and provide the RDFaExtractor interface for dealing solely with Jena model operations.