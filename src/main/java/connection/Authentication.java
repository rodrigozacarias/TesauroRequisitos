package connection;

public class Authentication {

    public String SERVER_URL = "http://localhost:10035";
    public String CATALOG_ID = "system";
    public String REPOSITORY_ID = "requirements";
    public String USERNAME = "admin";
    public String PASSWORD = "admin";
    public String TEMPORARY_DIRECTORY = "";

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

    public String getREPOSITORY_ID() {
        return REPOSITORY_ID;
    }

    public void setREPOSITORY_ID(String REPOSITORY_ID) {
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
}

