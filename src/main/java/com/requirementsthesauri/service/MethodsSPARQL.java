package com.requirementsthesauri.service;

import java.util.List;

public class MethodsSPARQL {

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

    public String insertDomainSparql(String domainID, String label, String prefLabel, String altLabel, String description,
                                     String linkDbpedia, String broaderDomainID, List<String> narrowerDomainID, List<String> narrowerRequirementID) {
        String queryInsert = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "PREFIX req: <localhost:8080/requirementsThesauri/requirements/>\n" +
                "INSERT DATA\n" +
                "{\n" +
                "  dom:"+ domainID +" 	rdf:type		skos:Concept ;\n" +
                "                rdfs:label	\""+label+"\" ;\n" +
                "                skos:preLabel	\""+prefLabel+"\" ;\n" +
                "                skos:altLabel	\""+altLabel+"\" ;\n" +
                "                skos:note	\""+description+"\" ;\n" +
                "                rdfs:seeAlso	dbr:"+linkDbpedia+" ;\n" +
                "                skos:broader	dom:"+broaderDomainID+" ;\n" ;
        if(!narrowerDomainID.isEmpty()){
            for (String nd: narrowerDomainID){
                queryInsert = queryInsert + "                skos:narrower	dom:"+nd+" ;\n";
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

    public String getAllDomainsSparqlSelect() {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "SELECT ?domain \n" +
                "WHERE\n" +
                "{\n" +
                "?domain rdf:type skos:Concept .\n" +
                "}\n" +
                "";

        return querySelect;
    }

    public String getDomainSparqlSelectNarrower(String domainID) {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "SELECT ?narrowerDomainID \n" +
                "WHERE\n" +
                "{\n" +
                "  dom:" + domainID + "	rdf:type		skos:Concept ;\n" +
                "                 skos:narrower  ?narrowerDomainID .\n" +
                "}\n" +
                "";

        return querySelect;
    }

    public String getDomainSparqlSelect(String domainID) {
        String query = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "SELECT ?label ?prefLabel ?altLabel ?description" +
                " ?linkDbpedia ?broaderDomainID ?narrowerDomainID ?narrowerRequirementID \n" +
                "WHERE{\n" +
                "\n" +
                "  dom:" + domainID + "	rdf:type		skos:Concept ;\n" +
                "                rdfs:label	?label ;\n" +
                "                skos:preLabel	?prefLabel ;\n" +
                "                skos:altLabel	?altLabel ;\n" +
                "                skos:note	?description ;\n" +
                "                rdfs:seeAlso	?linkDbpedia ;\n" +
                "                skos:broader	?broaderDomainID ;\n " +
                "                skos:narrower  ?narrowerDomainID ;\n" +
                "                skos:narrower	?narrowerRequirementID .\n" +
                "  \n" +
                "}\n" +
                "";
        return query;
    }

    public String getCompanySparqlDescribe(String domainID) {
        String query = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "DESCRIBE dom:" + domainID + "\n" +
                "WHERE\n" +
                "{\n" +
                "dom:" + domainID + " rdf:type skos:Concept  .\n" +
                "}\n" +
                "";

        return query;
    }

    public String updateDomainsSparql(String olDomainID, String domainID, String label, String prefLabel, String altLabel, String description,
                                      String linkDbpedia, String broaderDomainID, List<String> narrowerDomainID, List<String> narrowerRequirementID) {
        String queryUpdate = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
                "PREFIX dom: <localhost:8080/requirementsThesauri/domains/>\n" +
                "PREFIX req: <localhost:8080/requirementsThesauri/requirements/>\n" +
                "\n" +
                "DELETE \n" +
                "	{ dom:"+olDomainID+" ?p ?s }\n" +
                "WHERE\n" +
                "{ \n" +
                "  dom:"+olDomainID+" ?p ?s;\n" +
                " 		rdf:type skos:Concept .\n" +
                "};\n" +
                "\n" +
                "INSERT DATA\n" +
                "{\n" +
                "  dom:"+ domainID +" 	rdf:type		skos:Concept ;\n" +
                "                rdfs:label	\""+label+"\" ;\n" +
                "                skos:preLabel	\""+prefLabel+"\" ;\n" +
                "                skos:altLabel	\""+altLabel+"\" ;\n" +
                "                skos:note	\""+description+"\" ;\n" +
                "                rdfs:seeAlso	dbr:"+linkDbpedia+" ;\n" +
                "                skos:broader	dom:"+broaderDomainID+" ;\n" ;
        if(!narrowerDomainID.isEmpty()){
            for (String nd: narrowerDomainID){
                queryUpdate = queryUpdate + "                skos:narrower	dom:"+nd+" ;\n";
            }
        }
        if(!narrowerRequirementID.isEmpty()){
            for (String nr: narrowerRequirementID){

                queryUpdate = queryUpdate +  "                skos:narrower	req:"+nr+" ;\n";
            }
        }

        queryUpdate = queryUpdate + ".\n }";
        return queryUpdate;
    }
}
