package com.requirementsthesauri.service;

import com.requirementsthesauri.model.Requirement;
import com.requirementsthesauri.service.sparql.MethodsRequirementSPARQL;
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
            String broaderRequirementID = requirementsList.get(i).getBroaderRequirementID();
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
            if(uri.contains("requirements/")) {
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

    public ResponseEntity<?> getRequirement(String requirementID, String accept) {
        authentication.getAuthentication();

        String queryDescribe = requirementSPARQL.getRequirementSparqlDescribe(requirementID);

        Query query = QueryFactory.create(queryDescribe);
        QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

        Model rst = qx.execDescribe();
        if (rst.isEmpty()) {
            return new ResponseEntity("\"Please, choose a valid Domain ID.\"", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(accept.equals("application/json") || requirementSPARQL.isValidFormat(accept)==false) {

            String querySelect = requirementSPARQL.getRequirementSparqlSelect(requirementID);
            Query queryS = QueryFactory.create(querySelect);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryS);
            ResultSet results = qexec.execSelect();

            QuerySolution soln = results.nextSolution();
            String url = soln.getResource("url").toString();
            String label = soln.getLiteral("label").toString();
            String language = soln.getLiteral("language").toString();
            String prefLabel = soln.getLiteral("prefLabel").toString();
//            String altLabel = soln.getLiteral("altLabel").toString();
//            String problem = soln.getLiteral("problem").toString();
//            String context = soln.getLiteral("context").toString();
//            String template = soln.getLiteral("template").toString();
//            String example = soln.getLiteral("example").toString();
//            String broaderRequirementTypeID = soln.getResource("broaderRequirementTypeID").toString();
//            String broaderRequirementID = soln.getResource("broaderRequirementID").toString();


            String querySelectB = requirementSPARQL.getRequirementSparqlSelectBroader(requirementID);
            Query querySB = QueryFactory.create(querySelectB);
            QueryExecution qexecB = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySB);
            ResultSet resultsB = qexecB.execSelect();

            JsonArrayBuilder broaderDomainID = Json.createArrayBuilder();
            JsonArrayBuilder broaderSystemTypeID = Json.createArrayBuilder();
            String c = "broader";

            while(resultsB.hasNext()) {
                String uri = resultsB.nextSolution().getResource(c).getURI();
                if(uri.contains("domains")) {
                    broaderDomainID.add(uri);
                }else if(uri.contains("systemTypes")){
                    broaderSystemTypeID.add(uri);
                }
            }
            
            
            String querySelectN = requirementSPARQL.getRequirementSparqlSelectNarrower(requirementID);
            Query querySN = QueryFactory.create(querySelectN);
            QueryExecution qexecN = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySN);
            ResultSet resultsN = qexecN.execSelect();
            
            JsonArrayBuilder narrowerRequirementID = Json.createArrayBuilder();
            String s = "narrowerDomainID";

            while(resultsN.hasNext()) {
                narrowerRequirementID.add(resultsN.nextSolution().getResource(s).getURI());
            }
            

            JsonObject jobj = Json.createObjectBuilder()
                    .add("requirementID", requirementID)
                    .add("url", url)
                    .add("language", language)
                    .add("label",label)
                    .add("prefLabel", prefLabel)
//                    .add("altLabel", altLabel)
//                    .add("problem", problem)
//                    .add("context",context)
//                    .add("template", template)
//                    .add("example", example)
//                    .add("broaderRequirementTypeID", broaderRequirementTypeID)
//                    .add("broaderRequirementID", broaderRequirementID)
                    .add("broaderDomainID", broaderDomainID)
                    .add("broaderSystemTypeID", broaderSystemTypeID)
                    .add("narrowerRequirementID", narrowerRequirementID)
                    .build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(jobj);
            String output = new String(outputStream.toByteArray());
            writer.close();

            return new ResponseEntity<>(output, HttpStatus.OK);

        }else {

            String format = requirementSPARQL.convertFromAcceptToFormat(accept);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            rst.write(outputStream, format);
            String output = new String(outputStream.toByteArray());

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> updateRequirement(String oldRequirementID, Requirement newRequirement) {
        authentication.getAuthentication();

        String uri = "localhost:8080/requirementsThesauri/requirements/";

        String requirementID = newRequirement.getRequirementID();
        String label = newRequirement.getLabel();
        String language = newRequirement.getLanguage();
        String prefLabel = newRequirement.getPrefLabel();
        String altLabel = newRequirement.getAltLabel();
        String problem = newRequirement.getProblem();
        String context = newRequirement.getContext();
        String template = newRequirement.getTemplate();
        String example = newRequirement.getExample();
        String broaderRequirementTypeID = newRequirement.getBroaderRequirementTypeID();
        String broaderRequirementID = newRequirement.getBroaderRequirementTypeID();
        List<String> broaderDomainID = newRequirement.getBroaderDomainID();
        List<String> broaderSystemTypeID = newRequirement.getBroaderSystemTypeID();
        List<String> narrowerRequirementID = newRequirement.getNarrowerRequirementID();


        String queryUpdate = requirementSPARQL.updateRequirementSparql(oldRequirementID, requirementID, label, language, prefLabel, altLabel,
                problem, context, template, example, broaderRequirementTypeID,broaderRequirementID, broaderDomainID, broaderSystemTypeID,
                narrowerRequirementID);

        UpdateRequest request = UpdateFactory.create(queryUpdate);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();

        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        jsonArrayAdd.add(uri+requirementID);
        JsonArray ja = jsonArrayAdd.build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(outputStream);
        writer.writeArray(ja);
        String output = new String(outputStream.toByteArray());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    public void deleteRequirement(String requirementID) {
        authentication.getAuthentication();

        String deleteQuery = requirementSPARQL.deleteRequirementSparql(requirementID);

        UpdateRequest request = UpdateFactory.create(deleteQuery);
        UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
        up.execute();
    }

}
