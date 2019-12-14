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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DomainService {

    String sparqlEndpoint = "http://127.0.0.1:10035/catalogs/system/repositories/requirements/sparql";
    Authentication authentication = new Authentication();
    MethodsSPARQL methodsSPARQL = new MethodsSPARQL();

    public ResponseEntity<?> createDomain(List<Domain> domainsList){

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



            String queryUpdate = methodsSPARQL.insertDomainSparql(domainID, label, prefLabel, altLabel, description, linkDBpedia,
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
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }



    public ResponseEntity<?> getAllDomains(){
        authentication.getAuthentication();

        String querySelect = methodsSPARQL.getAllDomainsSparqlSelect();

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

        return new ResponseEntity<>(output, HttpStatus.OK);
    }



    public ResponseEntity<?> getDomain(String domainID, String accept) {
        authentication.getAuthentication();

        String queryDescribe = methodsSPARQL.getDomainSparqlDescribe(domainID);

        Query query = QueryFactory.create(queryDescribe);
        QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        Model rst = qx.execDescribe();
        if (rst.isEmpty()) {
            return new ResponseEntity("\"Please, choose a valid Domain ID.\"", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(accept.equals("application/json") || methodsSPARQL.isValidFormat(accept)==false) {

            String querySelect = methodsSPARQL.getDomainSparqlSelect(domainID);
            Query queryS = QueryFactory.create(querySelect);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryS);
            ResultSet results = qexec.execSelect();

            QuerySolution soln = results.nextSolution();
            String url = soln.getResource("url").toString();
            String label = soln.getLiteral("label").toString();
            String prefLabel = soln.getLiteral("prefLabel").toString();
            String altLabel = soln.getLiteral("altLabel").toString();
            String description = soln.getLiteral("description").toString();
            String linkDbpedia = soln.getResource("linkDbpedia").toString();
            String broaderDomainID = soln.getResource("broaderDomainID").toString();


            String querySelectN = methodsSPARQL.getDomainSparqlSelectNarrower(domainID);
            Query querySN = QueryFactory.create(querySelectN);
            QueryExecution qexecN = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySN);
            ResultSet resultsN = qexecN.execSelect();

            JsonArrayBuilder narrowerDomainID = Json.createArrayBuilder();
            JsonArrayBuilder narrowerRequirementID = Json.createArrayBuilder();
            String c = "narrowerDomainID";

            while(resultsN.hasNext()) {
                String uri = resultsN.nextSolution().getResource(c).getURI();
              if(uri.contains("domains")) {
                    narrowerDomainID.add(uri);
              }else{
                    narrowerRequirementID.add(uri);
              }
            }

//            String narrowerRequirementID = soln.getResource("narrowerRequirementID").toString();

            JsonObject jobj = Json.createObjectBuilder()
                    .add("domainID", domainID)
                    .add("url", url)
                    .add("label",label)
                    .add("prefLabel",prefLabel)
                    .add("altLabel",altLabel)
                    .add("description",description)
                    .add("linkDbpedia",linkDbpedia)
                    .add("broaderDomainID",broaderDomainID)
                    .add("narrowerDomainID",narrowerDomainID)
                    .add("narrowerRequirementID",narrowerRequirementID)
                    .build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(jobj);
            String output = new String(outputStream.toByteArray());
            writer.close();

            return new ResponseEntity<>(output, HttpStatus.OK);

        }else {

            String format = methodsSPARQL.convertFromAcceptToFormat(accept);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            rst.write(outputStream, format);
            String output = new String(outputStream.toByteArray());

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
    }


    public ResponseEntity<?> updateDomain(String oldDomainID, Domain newDomain) {
        authentication.getAuthentication();

        String uri = "localhost:8080/requirementsThesauri/domains/";

        String domainID = newDomain.getDomainID();
        String label = newDomain.getLabel();
        String prefLabel = newDomain.getPrefLabel();
        String altLabel = newDomain.getAltLabel();
        String description = newDomain.getDescription();
        String linkDBpedia = newDomain.getLinkDbpedia();
        String broaderDomainID = newDomain.getBroaderDomainID();
        List<String> narrowerDomainID = newDomain.getNarrowerDomainID();
        List<String> narrowerRequirementID = newDomain.getNarrowerRequirementID();


        String queryUpdate = methodsSPARQL.updateDomainsSparql(oldDomainID, domainID, label, prefLabel, altLabel, description, linkDBpedia,
                broaderDomainID, narrowerDomainID, narrowerRequirementID);

        UpdateRequest request = UpdateFactory.create(queryUpdate);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        jsonArrayAdd.add(uri+domainID);
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public void deleteDomain(String domainID) {
        authentication.getAuthentication();

        String deleteQuery = methodsSPARQL.deleteDomainSparql(domainID);

        UpdateRequest request = UpdateFactory.create(deleteQuery);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();
    }

    public List<Domain> getAllDomains2(){
        List<Domain> domains = new ArrayList<>();
        Domain domain = new Domain();
        domain.setLabel("Educação Básica");
        Domain domain2 = new Domain();
        domain2.setLabel("Farmácia");
        domains.add(domain);
        domains.add(domain2);
        return domains;
    }


}
