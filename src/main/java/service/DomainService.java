package service;

import com.franz.agraph.jena.AGGraph;
import com.franz.agraph.jena.AGModel;

public class DomainService {

    Authentication authentication = new Authentication();

    public String createDomain(){

        AGGraph graph = authentication.getConnectionDataBase();
        AGModel model = new AGModel(graph);




        return "";
    }

}
