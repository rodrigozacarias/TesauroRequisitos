package service;

import com.franz.agraph.jena.AGGraph;
import com.franz.agraph.jena.AGGraphMaker;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;

import java.util.ArrayList;
import java.util.List;

public class Authentication {

    public String SERVER_URL = "http://localhost:10035";
    public String CATALOG_ID = "system";
    public String REPOSITORY_ID = "requirements";
    public String USERNAME = "admin";
    public String PASSWORD = "admin";
    public String TEMPORARY_DIRECTORY = "";

    static Authentication authentication = new Authentication();

    public AGGraph getConnectionDataBase() {

        // Tests getting the repository up.
        println("\nStarting connection");
        AGServer server = new AGServer(SERVER_URL, USERNAME, PASSWORD);
        println("Available catalogs: " + server.listCatalogs());
        AGCatalog catalog = server.getCatalog(authentication.CATALOG_ID);
        println("Available repositories in catalog "
                + (catalog.getCatalogName()) + ": "
                + catalog.listRepositories());
        AGRepositoryConnection conn;
        if (catalog.listRepositories().isEmpty()) {
            conn = createRepository(catalog).getConnection();
        }else{
            AGRepository myRepository = server.getCatalog(CATALOG_ID).openRepository(REPOSITORY_ID);
            conn = myRepository.getConnection();
        }

        AGGraphMaker maker = new AGGraphMaker(conn);
        AGGraph graph = maker.getGraph();
        return graph;
    }


    public AGRepository createRepository(AGCatalog catalog){

        AGRepository myRepository = catalog.createRepository(REPOSITORY_ID);
        println("Got a repository.");
        myRepository.initialize();
        println("Initialized repository.");
        AGRepositoryConnection conn = myRepository.getConnection();
        println("Got a connection.");
        println("Repository " + (myRepository.getRepositoryID())
                + " is up! It contains " + (conn.size()) + " statements.");
        return myRepository;
    }


    public static void println(Object x) {
        System.out.println(x);
    }
    protected static void closeAll() {
        while (toClose.isEmpty() == false) {
            AGRepositoryConnection conn = toClose.get(0);
            close(conn);
            while (toClose.remove(conn)) {
            }
        }
    }

    private static List<AGRepositoryConnection> toClose = new ArrayList<AGRepositoryConnection>();

    static void close(AGRepositoryConnection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            System.err.println("Error closing repository connection: " + e);
            e.printStackTrace();
        }
    }

    protected static void closeBeforeExit(AGRepositoryConnection conn) {
        toClose.add(conn);
    }
}
