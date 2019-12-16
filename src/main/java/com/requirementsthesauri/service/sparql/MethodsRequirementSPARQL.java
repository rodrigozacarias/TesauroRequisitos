package com.requirementsthesauri.service.sparql;

import java.util.List;

public class MethodsRequirementSPARQL {

    public String convertFromAcceptToFormat(String accept) {
        String format = "";
        switch(accept) {
            case "application/ld+json":
                format = "JSON-LD";
                break;
            case "application/n-triples":
                format = "N-Triples";
                break;
            case "application/rdf+xml":
                format = "RDF/XML";
                break;
            case "application/turtle":
                format = "TURTLE";
                break;
            case "application/rdf+json":
                format = "RDF/JSON";
                break;
        }
        return format;
    }

    public boolean isValidFormat(String accept) {
        if (accept.equals("application/json") || accept.equals("application/ld+json") || accept.equals("application/n-triples") || accept.equals("application/rdf+xml") || accept.equals("application/turtle") || accept.equals("application/rdf+json") )
            return true;

        else
            return false;
    }

    public String insertRequirementSparql(String requirementID, String label, String language, String prefLabel, String altLabel,
                                          String problem, String context, String template, String example, String broaderRequirementTypeID,
                                          String broaderRequirementID, List<String> broaderDomainID, List<String> broaderSystemTypeID,
                                          List<String> narrowerRequirementID) {
        String queryInsert = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX schema: <http://schema.org/>\n" +
                "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "PREFIX req: <localhost:8080/requirementsThesauri/requirements/>\n" +
                "PREFIX sys: <localhost:8080/requirementsThesauri/systemTypes/>\n" +
                "PREFIX rqt: <localhost:8080/requirementsThesauri/requirementType/>\n" +
                "INSERT DATA\n" +
                "{\n" +
                "  req:"+ requirementID +" 	rdf:type		skos:Concept ;\n" +
                "                schema:url	req:"+ requirementID +" ;\n" +
                "                rdfs:label	\""+label+"\" ;\n" +
                "                dcmitype:language	\""+language+"\" ;\n" +
                "                skos:preLabel	\""+prefLabel+"\" ;\n" +
                "                skos:altLabel	\""+altLabel+"\" ;\n" +
                "                skos:scopeNote	\""+problem+"\" ;\n" +
                "                skos:related	\""+context+"\" ;\n" +
                "                skos:definition	\""+template+"\" ;\n" +
                "                skos:example	\""+example+"\" ;\n" +
                "                skos:broader	rqt"+broaderRequirementTypeID+" ;\n" +
                "                skos:broader	req"+broaderRequirementID+" ;\n";
        if(!broaderDomainID.isEmpty()){
            for (String bd: broaderDomainID){
                queryInsert = queryInsert + "                skos:broader	dom:"+bd+" ;\n";
            }
        }
        if(!broaderSystemTypeID.isEmpty()){
            for (String bs: broaderSystemTypeID){
                queryInsert = queryInsert + "                skos:broader	sys:"+bs+" ;\n";
            }
        }
        if(!narrowerRequirementID.isEmpty()){
            for (String nr: narrowerRequirementID){

                queryInsert = queryInsert +  "                skos:narrower	req:"+nr+" ;\n";
            }
        }

        queryInsert = queryInsert + ".\n }";
        return queryInsert;

    }

    public String getAllRequirementsSparqlSelect() {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?requirement \n" +
                "WHERE\n" +
                "{\n" +
                "?requirement rdf:type skos:Concept .\n" +
                "}\n" +
                "";

        return querySelect;
    }

    public String getRequirementSparqlSelectNarrower(String requirementID) {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX req: <localhost:8080/requirementsThesauri/requirements/>\n" +
                "SELECT ?narrower \n" +
                "WHERE\n" +
                "{\n" +
                "  req:" + requirementID + "	rdf:type		skos:Concept ;\n" +
                "                 skos:narrower  ?narrower .\n" +
                "}\n" +
                "";

        return querySelect;
    }
    public String getRequirementSparqlSelectBroader(String requirementID) {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX req: <localhost:8080/requirementsThesauri/requirements/>\n" +
                "SELECT ?broader \n" +
                "WHERE\n" +
                "{\n" +
                "  req:" + requirementID + "	rdf:type		skos:Concept ;\n" +
                "                 skos:broader  ?broader .\n" +
                "}\n" +
                "";

        return querySelect;
    }
}
