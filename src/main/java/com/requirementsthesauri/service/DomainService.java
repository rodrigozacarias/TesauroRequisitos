package com.requirementsthesauri.service;

import com.requirementsthesauri.model.Domain;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.json.*;
import javax.xml.ws.Response;
import java.io.ByteArrayOutputStream;
import java.util.List;


public class DomainService {

    String sparqlEndpoint = "http://127.0.0.1:10035/catalogs/system/repositories/requirements/sparql";
    Authentication authentication = new Authentication();
    String id = "";

    public String createDomain(List<Domain> domainsList){

        authentication.getAuthentication();

        int TAM = domainsList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/domains/";

        for(int i=0; i<TAM; i++) {
            String domainID = domainsList.get(i).getDomainID();
            String label = domainsList.get(i).getLabel();
            String prefLabel = domainsList.get(i).getPrefLabel();
            String altLabel = domainsList.get(i).getAltLabel();
            String description = domainsList.get(i).getDescription();
            String linkDBpedia = domainsList.get(i).getLinkDbpedia();
            String broaderDomainID = domainsList.get(i).getBroaderDomainID();
            List<String> narrowerDomainID = domainsList.get(i).getNarrowerDomainID();
            List<String> narrowerRequirementID = domainsList.get(i).getNarrowerRequirementID();



            String queryUpdate = insertDomainSparql(domainID, label, prefLabel, altLabel, description, linkDBpedia,
                    broaderDomainID, narrowerDomainID, narrowerRequirementID);

            UpdateRequest request = UpdateFactory.create(queryUpdate);
            UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
            up.execute();

            jsonArrayAdd.add(uri+domainID);

        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return output;
    }

    public String insertDomainSparql(String domainID, String label, String prefLabel, String altLabel, String description,
                                     String linkDbpedia, String broaderDomainID, List<String> narrowerDomainID, List<String> narrowerRequirementID) {
        String queryInsert = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
                "PREFIX uri: <localhost:8080/requirementsThesauri/domains/>\n" +
                "INSERT DATA\n" +
                "{\n" +
                "  uri:"+ domainID +" 	rdf:type		skos:Concept ;\n" +
                "                rdfs:label	\""+label+"\" ;\n" +
                "                skos:preLabel	\""+prefLabel+"\" ;\n" +
                "                skos:altLabel	\""+altLabel+"\" ;\n" +
                "                skos:note	\""+description+"\" ;\n" +
                "                rdfs:seeAlso	dbr:"+linkDbpedia+" ;\n" +
                "                skos:broader	<"+broaderDomainID+"> ;\n" ;
        if(!narrowerDomainID.isEmpty()){
            for (String nd: narrowerDomainID){
                queryInsert = queryInsert + "                skos:narrower	<"+nd+"> ;\n";
            }
        }
        if(!narrowerRequirementID.isEmpty()){
            for (String nr: narrowerRequirementID){

                queryInsert = queryInsert +  "                skos:narrower	<"+nr+"> ;\n";
            }
        }

        queryInsert = queryInsert + ".\n }";
        return queryInsert;

    }

    public String getAllDomains(){
        authentication.getAuthentication();

        String querySelect = getAllDomainsSparqlSelect();

        Query query = QueryFactory.create(querySelect);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        ResultSet results = qexec.execSelect();
        //return ResultSetFormatter.asText(results);

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String c = "domain";
        while(results.hasNext()) {
            jsonArrayAdd.add(results.nextSolution().getResource(c).getURI());
        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());

        return output;
    }


    public String getAllDomainsSparqlSelect() {
        String querySelect = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                "PREFIX uri: <localhost:8080/requirementsThesauri/domains/>\n" +
                "SELECT ?domain\r\n" +
                "WHERE\r\n" +
                "{\r\n" +
                "?domain rdf:type skos:Concept .\r\n" +
                "}\r\n" +
                "";

        return querySelect;
    }

    public ResponseEntity<?> getDomain(String domainID, String accept) {
        authentication.getAuthentication();

        String queryDescribe = getCompanySparqlDescribe(domainID);

        Query query = QueryFactory.create(queryDescribe);
        QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        Model rst = qx.execDescribe();
        if (rst.isEmpty()) {
            return ResponseEntity.status(422).build();
        }

        if(accept.equals("application/json") || isValidFormat(accept)==false) {

            String querySelect = getCompanySparqlSelect(domainID);
            Query queryS = QueryFactory.create(querySelect);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryS);
            ResultSet results = qexec.execSelect();

            QuerySolution soln = results.nextSolution();
            String companyURL = soln.getResource("companyURL").toString() ;
            String legalName = soln.getLiteral("legalName").toString() ;
            String email = soln.getResource("email").toString() ;
            String catalogURI = soln.getResource("catalogURI").toString() ;

            JsonObject jobj = Json.createObjectBuilder()
                    .add("id", domainID)
                    .add("companyURL",companyURL)
                    .add("legalName",legalName)
                    .add("email",email)
                    .add("catalogURI",catalogURI)
                    .build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(jobj);
            String output = new String(outputStream.toByteArray());
            writer.close();

            return ResponseEntity.status(422).build();

        }else {

            String format = convertFromAcceptToFormat(accept);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            rst.write(outputStream, format);
            String output = new String(outputStream.toByteArray());

            return ResponseEntity.status(422).build();
        }
    }

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

    public String getCompanySparqlSelect(String companyID) {
        String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +
                "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#>\r\n" +
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                "PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
                "\r\n" +
                "SELECT ?companyURL ?email ?catalogURI ?legalName\r\n" +
                "\r\n" +
                "WHERE{\r\n" +
                "\r\n" +
                "  exco:"+companyID+"	rdf:type		gr:BusinessEntity;\r\n" +
                "                    vcard:hasURL	?companyURL;\r\n" +
                "                    vcard:hasEmail	?email;\r\n" +
                "       <http://schema.org/catalog>	?catalogURI;\r\n" +
                "                    gr:legalName	?legalName	.\r\n" +
                "  \r\n" +
                "}\r\n" +
                "";
        return query;
    }

    public String getCompanySparqlDescribe(String companyID) {
        String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                "PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
                "\r\n" +
                "DESCRIBE exco:" + companyID + "\r\n" +
                "WHERE\r\n" +
                "{\r\n" +
                "exco:" + companyID + " rdf:type gr:BusinessEntity  .\r\n" +
                "}\r\n" +
                "";

        return query;
    }
    /*public List<Domain> getAllDomains(){
        List<Domain> domains = new ArrayList<>();
        Domain domain = new Domain();
        domain.setLabel("Segurança");
        Domain domain2 = new Domain();
        domain2.setLabel("Confiabilidade");
        domains.add(domain);
        domains.add(domain2);
        return domains;
    }*/


}
