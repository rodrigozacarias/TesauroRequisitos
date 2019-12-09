package com.requirementsthesauri.service;

import com.franz.agraph.jena.AGGraph;
import com.franz.agraph.jena.AGModel;
import com.requirementsthesauri.model.Domain;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import javax.json.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DomainService {

    String sparqlEndpoint = "http://http://localhost:10035/catalogs/system/repositories/requirements#query";
    Authentication authentication = new Authentication();
    String id = "";

    public List<Domain> createDomain(List<Domain> domainsList){

        AGGraph graph = authentication.getConnectionDataBase();

        int TAM = domainsList.size();
        JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
        String uri = "localhost:8080/requirementsThesauri/domains/";

        for(int i=0; i<TAM; i++) {
            String domainID = domainsList.get(i).getDomainID();
            String label = domainsList.get(i).getLabel();
            String prefLabel = domainsList.get(i).getPrefLabel();
            String altLabel = domainsList.get(i).getAltLabel();
            String description = domainsList.get(i).getDescription();
            String broaderDomainID = domainsList.get(i).getBroaderDomainID();
            List<String> narrowerDomainID = domainsList.get(i).getNarrowerDomainID();
            List<String> narrowerRequirementID = domainsList.get(i).getNarrowerRequirementID();



            String queryUpdate = insertDomainSparql(domainID, label, prefLabel, altLabel, description,
                    broaderDomainID, narrowerDomainID, narrowerRequirementID);

            UpdateRequest request = UpdateFactory.create(queryUpdate);
            UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
            up.execute();

        }
        return domainsList;
    }

    public String insertDomainSparql(String domainID, String label, String prefLabel, String altLabel, String description,
                                     String broaderDomainID, List<String> narrowerDomainID, List<String> narrowerRequirementID) {
        String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +
                "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#>\r\n" +
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                "PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
                "\r\n" +
                "\r\n" +
                "INSERT DATA\r\n" +
                "{ \r\n"
                /*"  exco:"+//companyID+" 	rdf:type		gr:BusinessEntity;\r\n" +
                "                vcard:hasURL	<"+companyURL+">;\r\n" +
                "                vcard:hasEmail	<"+email+">;\r\n" +
                "               <http://schema.org/catalog>	<"+catalogURI+">;\r\n" +
                "                gr:legalName	'"+legalName+"'   .           \r\n" +
                "}"*/;

        return queryInsert;

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


}
