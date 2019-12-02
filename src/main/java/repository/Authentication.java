package repository;

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
    public List<String> REPOSITORY_ID = new ArrayList<>();
    public String USERNAME = "admin";
    public String PASSWORD = "admin";
    public String TEMPORARY_DIRECTORY = "";

    static Authentication authentication = new Authentication();

    public Authentication() {
    }

    public String getSERVER_URL() {
        return SERVER_URL;
    }

    public void setSERVER_URL(String SERVER_URL) {
        this.SERVER_URL = SERVER_URL;
    }

    public String getCATALOG_ID() {
        return CATALOG_ID;
    }

    public void setCATALOG_ID(String CATALOG_ID) {
        this.CATALOG_ID = CATALOG_ID;
    }

    public List<String> getREPOSITORY_ID() {
        List<String> repositories = new ArrayList<>();
        repositories.add("requirements");
        repositories.add("domains");
        repositories.add("requirementTypes");
        repositories.add("systemTypes");
        setREPOSITORY_ID(repositories);
        return REPOSITORY_ID;
    }

    public void setREPOSITORY_ID(List<String> REPOSITORY_ID) {
        this.REPOSITORY_ID = REPOSITORY_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getTEMPORARY_DIRECTORY() {
        return TEMPORARY_DIRECTORY;
    }

    public void setTEMPORARY_DIRECTORY(String TEMPORARY_DIRECTORY) {
        this.TEMPORARY_DIRECTORY = TEMPORARY_DIRECTORY;
    }

    public void getConnectionDataBase(){
        // Tests getting the repository up.
        println("\nStarting example1().");
        AGServer server = new AGServer(authentication.getSERVER_URL(), authentication.getUSERNAME(), authentication.getPASSWORD());
        println("Available catalogs: " + server.listCatalogs());
        AGCatalog catalog = server.getCatalog(authentication.getCATALOG_ID());
        println("Available repositories in catalog "
                + (catalog.getCatalogName()) + ": "
                + catalog.listRepositories());
        closeAll();
        if(catalog.listRepositories().isEmpty()){
            createRepository(catalog);
        }

    }

    public void createRepository(AGCatalog catalog){
        for(String rep: authentication.getREPOSITORY_ID()){

            AGRepository myRepository = catalog.createRepository(rep);
            println("Got a repository.");
            myRepository.initialize();
            println("Initialized repository.");
            AGRepositoryConnection conn = myRepository.getConnection();
            closeBeforeExit(conn);
            println("Got a connection.");
            println("Repository " + (myRepository.getRepositoryID())
                    + " is up! It contains " + (conn.size()) + " statements.");
            AGGraphMaker maker = new AGGraphMaker(conn);
            println("Got a graph maker for the connection.");
            List<String> indices = conn.listValidIndices();
            println("All valid triple indices: " + indices);
            indices = conn.listIndices();
            println("Current triple indices: " + indices);
            println("Removing graph indices...");
            conn.dropIndex("gospi");
            conn.dropIndex("gposi");
            conn.dropIndex("gspoi");
            indices = conn.listIndices();
            println("Current triple indices: " + indices);
            println("Adding one graph index back in...");
            conn.addIndex("gspoi");
            indices = conn.listIndices();
            println("Current triple indices: " + indices);
        /*if (close) {
            // tidy up
            maker.close();
            conn.close();
            myRepository.shutDown();
            return null;
        }*/
        }
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

