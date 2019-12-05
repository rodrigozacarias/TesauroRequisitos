package com.requirementsthesauri.service;

import com.franz.agraph.jena.AGGraph;
import com.franz.agraph.jena.AGModel;
import com.requirementsthesauri.model.Domain;


public class DomainService {

    Authentication authentication = new Authentication();

    public String createDomain(Domain domain){

        AGGraph graph = authentication.getConnectionDataBase();
        AGModel model = new AGModel(graph);




        return "teste";
    }

    public Domain getAllDomains(){
        Domain domain = new Domain();
        domain.setLabel("Segurança");
        return domain;
    }

}
