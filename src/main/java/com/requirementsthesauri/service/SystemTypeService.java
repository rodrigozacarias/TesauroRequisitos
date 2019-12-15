package com.requirementsthesauri.service;

import com.requirementsthesauri.model.SystemType;
import com.requirementsthesauri.service.sparql.MethodsSystemTypeSPARQL;
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
import java.util.List;

public class SystemTypeService {

    String sparqlEndpoint = "http://127.0.0.1:10035/catalogs/system/repositories/requirements/sparql";
    Authentication authentication = new Authentication();
    MethodsSystemTypeSPARQL methodsSystemTypeSPARQL = new MethodsSystemTypeSPARQL();

    public ResponseEntity<?> createSystemType(List<SystemType> systemTypesList){

        authentication.getAuthentication();

        int TAM = systemTypesList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/systemTypes/";

        for(int i=0; i<TAM; i++) {
            String systemTypeID = systemTypesList.get(i).getSystemTypeID();
            String label = systemTypesList.get(i).getLabel();
            String prefLabel = systemTypesList.get(i).getPrefLabel();
            String altLabel = systemTypesList.get(i).getAltLabel();
            String description = systemTypesList.get(i).getDescription();
            String linkDBpedia = systemTypesList.get(i).getLinkDbpedia();
            String broaderSystemTypeID = systemTypesList.get(i).getBroaderSystemTypeID();
            List<String> narrowerSystemTypeID = systemTypesList.get(i).getNarrowerSystemTypeID();
            List<String> narrowerRequirementID = systemTypesList.get(i).getNarrowerRequirementID();



            String queryUpdate = methodsSystemTypeSPARQL.insertSystemTypeSparql(systemTypeID, label, prefLabel, altLabel, description, linkDBpedia,
                    broaderSystemTypeID, narrowerSystemTypeID, narrowerRequirementID);

            UpdateRequest request = UpdateFactory.create(queryUpdate);
            UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
            up.execute();

            jsonArrayAdd.add(uri+systemTypeID);

        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllSystemTypes(){
        authentication.getAuthentication();

        String querySelect = methodsSystemTypeSPARQL.getAllSystemTypesSparqlSelect();

        Query query = QueryFactory.create(querySelect);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        ResultSet results = qexec.execSelect();
        //return ResultSetFormatter.asText(results);

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String c = "system";
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

    public ResponseEntity<?> getSystemType(String systemTypeID, String accept) {
        authentication.getAuthentication();

        String queryDescribe = methodsSystemTypeSPARQL.getSystemTypeSparqlDescribe(systemTypeID);

        Query query = QueryFactory.create(queryDescribe);
        QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        Model rst = qx.execDescribe();
        if (rst.isEmpty()) {
            return new ResponseEntity("\"Please, choose a valid Domain ID.\"", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(accept.equals("application/json") || methodsSystemTypeSPARQL.isValidFormat(accept)==false) {

            String querySelect = methodsSystemTypeSPARQL.getSystemTypeSparqlSelect(systemTypeID);
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
            String broaderSystemTypeID = soln.getResource("broaderSystemTypeID").toString();


            String querySelectN = methodsSystemTypeSPARQL.getSystemTypeSparqlSelectNarrower(systemTypeID);
            Query querySN = QueryFactory.create(querySelectN);
            QueryExecution qexecN = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySN);
            ResultSet resultsN = qexecN.execSelect();

            JsonArrayBuilder narrowerSystemTypeID = Json.createArrayBuilder();
            JsonArrayBuilder narrowerRequirementID = Json.createArrayBuilder();
            String c = "narrowerSystemTypeID";

            while(resultsN.hasNext()) {
                String uri = resultsN.nextSolution().getResource(c).getURI();
                if(uri.contains("systemTypes")) {
                    narrowerSystemTypeID.add(uri);
                }else{
                    narrowerRequirementID.add(uri);
                }
            }


            JsonObject jobj = Json.createObjectBuilder()
                    .add("systemTypeID", systemTypeID)
                    .add("url", url)
                    .add("label",label)
                    .add("prefLabel",prefLabel)
                    .add("altLabel",altLabel)
                    .add("description",description)
                    .add("linkDbpedia",linkDbpedia)
                    .add("broaderSystemTypeID",broaderSystemTypeID)
                    .add("narrowerSystemTypeID",narrowerSystemTypeID)
                    .add("narrowerRequirementID",narrowerRequirementID)
                    .build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(jobj);
            String output = new String(outputStream.toByteArray());
            writer.close();

            return new ResponseEntity<>(output, HttpStatus.OK);

        }else {

            String format = methodsSystemTypeSPARQL.convertFromAcceptToFormat(accept);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            rst.write(outputStream, format);
            String output = new String(outputStream.toByteArray());

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> updateSystemType(String oldSystemTypeID, SystemType newSystemType) {
        authentication.getAuthentication();

        String uri = "localhost:8080/requirementsThesauri/systemTypes/";

        String systemTypeID = newSystemType.getSystemTypeID();
        String label = newSystemType.getLabel();
        String prefLabel = newSystemType.getPrefLabel();
        String altLabel = newSystemType.getAltLabel();
        String description = newSystemType.getDescription();
        String linkDBpedia = newSystemType.getLinkDbpedia();
        String broaderSystemTypeID = newSystemType.getBroaderSystemTypeID();
        List<String> narrowerSystemTypeID = newSystemType.getNarrowerSystemTypeID();
        List<String> narrowerRequirementID = newSystemType.getNarrowerRequirementID();


        String queryUpdate = methodsSystemTypeSPARQL.updateSystemTypeSparql(oldSystemTypeID, systemTypeID, label, prefLabel, altLabel, description, linkDBpedia,
                broaderSystemTypeID, narrowerSystemTypeID, narrowerRequirementID);

        UpdateRequest request = UpdateFactory.create(queryUpdate);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        jsonArrayAdd.add(uri+systemTypeID);
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public void deleteSystemType(String systemTypeID) {
        authentication.getAuthentication();

        String deleteQuery = methodsSystemTypeSPARQL.deleteSystemTypeSparql(systemTypeID);

        UpdateRequest request = UpdateFactory.create(deleteQuery);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();
    }

}
