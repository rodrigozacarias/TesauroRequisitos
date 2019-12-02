import com.franz.agraph.jena.AGGraphMaker;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import connection.Authentication;

import java.util.ArrayList;
import java.util.List;

public class Application {


    public static void main(String[] args) {

        Authentication authentication = new Authentication();

        // Tests getting the repository up.
        println("\nStarting example1().");
        AGServer server = new AGServer(authentication.getSERVER_URL(), authentication.getUSERNAME(), authentication.getPASSWORD());
        println("Available catalogs: " + server.listCatalogs());
        AGCatalog catalog = server.getCatalog(authentication.getCATALOG_ID());
        println("Available repositories in catalog "
                + (catalog.getCatalogName()) + ": "
                + catalog.listRepositories());
        closeAll();
        AGRepository myRepository = catalog.createRepository(authentication.getREPOSITORY_ID());
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