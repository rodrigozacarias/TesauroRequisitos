package com.requirementsthesauri.service;

import com.requirementsthesauri.model.RequirementType;
import com.requirementsthesauri.service.sparql.MethodsRequirementTypeSPARQL;
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

public class RequirementTypeService {

    String sparqlEndpoint = "http://127.0.0.1:10035/catalogs/system/repositories/requirements/sparql";
    Authentication authentication = new Authentication();
    MethodsRequirementTypeSPARQL methodsRequirementTypeSPARQL = new MethodsRequirementTypeSPARQL();

    public ResponseEntity<?> createRequirementType(List<RequirementType> requirementTypesList){

        authentication.getAuthentication();

        int TAM = requirementTypesList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/requirementTypes/";

        for(int i=0; i<TAM; i++) {
            String requirementTypeID = requirementTypesList.get(i).getRequirementTypeID();
            String label = requirementTypesList.get(i).getLabel();
            String prefLabel = requirementTypesList.get(i).getPrefLabel();
            String altLabel = requirementTypesList.get(i).getAltLabel();
            String description = requirementTypesList.get(i).getDescription();
            String linkDBpedia = requirementTypesList.get(i).getLinkDbpedia();
            String broaderRequirementTypeID = requirementTypesList.get(i).getBroaderRequirementTypeID();
            List<String> narrowerRequirementTypeID = requirementTypesList.get(i).getNarrowerRequirementTypeID();
            List<String> narrowerRequirementID = requirementTypesList.get(i).getNarrowerRequirementID();



            String queryUpdate = methodsRequirementTypeSPARQL.insertRequirementTypeSparql(requirementTypeID, label, prefLabel, altLabel, description, linkDBpedia,
                    broaderRequirementTypeID, narrowerRequirementTypeID, narrowerRequirementID);

            UpdateRequest request = UpdateFactory.create(queryUpdate);
            UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
            up.execute();

            jsonArrayAdd.add(uri+requirementTypeID);

        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllRequirementTypes(){
        authentication.getAuthentication();

        String querySelect = methodsRequirementTypeSPARQL.getAllRequirementTypesSparqlSelect();

        Query query = QueryFactory.create(querySelect);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        ResultSet results = qexec.execSelect();
        //return ResultSetFormatter.asText(results);

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String c = "requirementTypes";
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

    public ResponseEntity<?> getRequirementType(String requirementTypeID, String accept) {
        authentication.getAuthentication();

        String queryDescribe = methodsRequirementTypeSPARQL.getRequirementTypeSparqlDescribe(requirementTypeID);

        Query query = QueryFactory.create(queryDescribe);
        QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        Model rst = qx.execDescribe();
        if (rst.isEmpty()) {
            return new ResponseEntity("\"Please, choose a valid Domain ID.\"", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(accept.equals("application/json") || methodsRequirementTypeSPARQL.isValidFormat(accept)==false) {

            String querySelect = methodsRequirementTypeSPARQL.getRequirementTypeSparqlSelect(requirementTypeID);
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
            String broaderRequirementTypeID = soln.getResource("broaderRequirementTypeID").toString();


            String querySelectN = methodsRequirementTypeSPARQL.getRequirementTypeSparqlSelectNarrower(requirementTypeID);
            Query querySN = QueryFactory.create(querySelectN);
            QueryExecution qexecN = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySN);
            ResultSet resultsN = qexecN.execSelect();

            JsonArrayBuilder narrowerRequirementTypeID = Json.createArrayBuilder();
            JsonArrayBuilder narrowerRequirementID = Json.createArrayBuilder();
            String c = "narrowerRequirementTypeID";

            while(resultsN.hasNext()) {
                String uri = resultsN.nextSolution().getResource(c).getURI();
                if(uri.contains("requirementTypes")) {
                    narrowerRequirementTypeID.add(uri);
                }else{
                    narrowerRequirementID.add(uri);
                }
            }


            JsonObject jobj = Json.createObjectBuilder()
                    .add("RequirementTypeID", requirementTypeID)
                    .add("url", url)
                    .add("label",label)
                    .add("prefLabel",prefLabel)
                    .add("altLabel",altLabel)
                    .add("description",description)
                    .add("linkDbpedia",linkDbpedia)
                    .add("broaderRequirementTypeID",broaderRequirementTypeID)
                    .add("narrowerRequirementTypeID",narrowerRequirementTypeID)
                    .add("narrowerRequirementID",narrowerRequirementID)
                    .build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(jobj);
            String output = new String(outputStream.toByteArray());
            writer.close();

            return new ResponseEntity<>(output, HttpStatus.OK);

        }else {

            String format = methodsRequirementTypeSPARQL.convertFromAcceptToFormat(accept);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            rst.write(outputStream, format);
            String output = new String(outputStream.toByteArray());

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> updateRequirementType(String oldRequirementTypeID, RequirementType newRequirementType) {
        authentication.getAuthentication();

        String uri = "localhost:8080/requirementsThesauri/requirementTypes/";

        String requirementTypeID = newRequirementType.getRequirementTypeID();
        String label = newRequirementType.getLabel();
        String prefLabel = newRequirementType.getPrefLabel();
        String altLabel = newRequirementType.getAltLabel();
        String description = newRequirementType.getDescription();
        String linkDBpedia = newRequirementType.getLinkDbpedia();
        String broaderRequirementTypeID = newRequirementType.getBroaderRequirementTypeID();
        List<String> narrowerRequirementTypeID = newRequirementType.getNarrowerRequirementTypeID();
        List<String> narrowerRequirementID = newRequirementType.getNarrowerRequirementID();


        String queryUpdate = methodsRequirementTypeSPARQL.updateRequirementTypeSparql(oldRequirementTypeID, requirementTypeID, label, prefLabel, altLabel, description, linkDBpedia,
                broaderRequirementTypeID, narrowerRequirementTypeID, narrowerRequirementID);

        UpdateRequest request = UpdateFactory.create(queryUpdate);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        jsonArrayAdd.add(uri+requirementTypeID);
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public void deleteRequirementType(String RequirementTypeID) {
        authentication.getAuthentication();

        String deleteQuery = methodsRequirementTypeSPARQL.deleteRequirementTypeSparql(RequirementTypeID);

        UpdateRequest request = UpdateFactory.create(deleteQuery);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();
    }

}
    

