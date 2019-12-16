package com.requirementsthesauri.service;

import com.requirementsthesauri.model.Domain;
import com.requirementsthesauri.model.Requirement;
import com.requirementsthesauri.service.sparql.MethodsRequirementSPARQL;
import org.apache.jena.query.*;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class RequirementService {

    String sparqlEndpoint = "http://127.0.0.1:10035/catalogs/system/repositories/requirements/sparql";
    Authentication authentication = new Authentication();
    MethodsRequirementSPARQL requirementSPARQL = new MethodsRequirementSPARQL();

    public ResponseEntity<?> createRequirement(List<Requirement> requirementsList){

        authentication.getAuthentication();

        int TAM = requirementsList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/requirements/";

        for(int i=0; i<TAM; i++) {
            String requirementID = requirementsList.get(i).getRequirementID();
            String label = requirementsList.get(i).getLabel();
            String language = requirementsList.get(i).getLanguage();
            String prefLabel = requirementsList.get(i).getPrefLabel();
            String altLabel = requirementsList.get(i).getAltLabel();
            String problem = requirementsList.get(i).getProblem();
            String context = requirementsList.get(i).getContext();
            String template = requirementsList.get(i).getTemplate();
            String example = requirementsList.get(i).getExample();
            String broaderRequirementTypeID = requirementsList.get(i).getBroaderRequirementTypeID();
            String broaderRequirementID = requirementsList.get(i).getBroaderRequirementTypeID();
            List<String> broaderDomainID = requirementsList.get(i).getBroaderDomainID();
            List<String> broaderSystemTypeID = requirementsList.get(i).getBroaderSystemTypeID();
            List<String> narrowerRequirementID = requirementsList.get(i).getNarrowerRequirementID();



            String queryUpdate = requirementSPARQL.insertRequirementSparql(requirementID, label, language, prefLabel, altLabel,
                    problem, context, template, example, broaderRequirementTypeID,broaderRequirementID, broaderDomainID, broaderSystemTypeID,
                    narrowerRequirementID);

            UpdateRequest request = UpdateFactory.create(queryUpdate);
            UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
            up.execute();

            jsonArrayAdd.add(uri+requirementID);

        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllRequirements(){
        authentication.getAuthentication();

        String querySelect = requirementSPARQL.getAllRequirementsSparqlSelect();

        Query query = QueryFactory.create(querySelect);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        ResultSet results = qexec.execSelect();
        //return ResultSetFormatter.asText(results);

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String c = "requirement";
        while(results.hasNext()) {
            String uri = results.nextSolution().getResource(c).getURI();
            if(uri.contains("requirements")) {
                jsonArrayAdd.add(uri);
            }
        }
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

}
