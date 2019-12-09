package com.requirementsthesauri.service;

import com.franz.agraph.jena.AGGraph;
import com.franz.agraph.jena.AGModel;
import com.requirementsthesauri.model.Domain;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import javax.json.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DomainService {

    Authentication authentication = new Authentication();
    String id = "";

    public List<Domain> createDomain(List<Domain> domainsList){

        AGGraph graph = authentication.getConnectionDataBase();

        int TAM = domainsList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/domains/";

        //AGModel model = new AGModel(graph);
        /*Iterator<?> subjects = model.listSubjects();
        Iterator<Map.Entry<String, JsonValue>> json_values = domains.entrySet().iterator();

        id = newId(json_values);
        if (existId(subjects, id) || (id.equals(""))) {
            return null;
        }

        Resource resource =	model.createResource("http://localhost:8080/requirementsThesauri/domains/" + id);
        Property type = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        Resource resourceLocation =	model.createResource("http://www.w3.org/2004/02/skos/core#Concept" );
        model.add(resource, type, resourceLocation);
        Iterator<Map.Entry<String, JsonValue>> iterator = domains.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonValue> entry = iterator.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            JsonValue Jvalue = entry.getValue();


                    model.add(resource, model.getProperty(entry.getKey()), value.substring(1,value.length()-1));


        }


        authentication.conn.close();
        return resource.getURI();*/
        return domainsList;
    }

    public List<Domain> getAllDomains(){
        List<Domain> domains = new ArrayList<>();
        Domain domain = new Domain();
        domain.setLabel("Segurança");
        Domain domain2 = new Domain();
        domain2.setLabel("Confiabilidade");
        domains.add(domain);
        domains.add(domain2);
        return domains;
    }

    private JsonObject expandJson(JsonObject obj){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonOB = factory.createObjectBuilder();

        Iterator<Map.Entry<String, JsonValue>> iterator = obj.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, JsonValue> entry = iterator.next();
            String prefix = setPrefix(entry.getKey().toString());
            jsonOB.add(prefix + entry.getKey(), entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
        }
        return jsonOB.build();
    }


    public String setPrefix(String property) {
        String prefix = "";
        if (property.equals("label")) {
            prefix = "http://www.w3.org/2000/01/rdf-schema#";
        }else if (property.equals("breeder")) {
            prefix = "http://dbpedia.org/ontology/";
        }else if (property.equals("domainID")){
            prefix = "";
        }else{
            prefix = "http://www.w3.org/2004/02/skos/core#";
        }
        return prefix;
    }

    private static String newId (Iterator<Map.Entry<String, JsonValue>> json_values) {
        String newId = "";
        while(json_values.hasNext())
        {
            Map.Entry<String, JsonValue> entry = json_values.next();
            if (entry.getKey().toString().equals("id")){
                String id = entry.getValue().toString();
                newId = id.substring(1,id.length()-1);
            }
        }
        return newId;
    }

    private static Boolean existId(Iterator<?> subjects, String id){
        while(subjects.hasNext()){
            Resource r = (Resource) subjects.next();
            if(r.toString().contains("http://localhost:8080/requirementsThesauri/domains/")){/*Verify if it's the ontology statement*/
                String existId = r.toString().substring(49);
                if (existId.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
