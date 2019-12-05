package com.requirementsthesauri.model;

import java.util.List;

public class SystemType {

    private String systemTypeID;
    private String label;
    private String prefLabel;
    private String altLabel;
    private String description;
    private String broaderSystemTypeID;
    private List<String> narrowerSystemTypeID;
    private List<String> narrowerRequirementID;

    public SystemType() {
    }

    public String getSystemTypeID() {
        return systemTypeID;
    }

    public void setSystemTypeID(String systemTypeID) {
        this.systemTypeID = systemTypeID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    public String getAltLabel() {
        return altLabel;
    }

    public void setAltLabel(String altLabel) {
        this.altLabel = altLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBroaderSystemTypeID() {
        return broaderSystemTypeID;
    }

    public void setBroaderSystemTypeID(String broaderSystemTypeID) {
        this.broaderSystemTypeID = broaderSystemTypeID;
    }

    public List<String> getNarrowerSystemTypeID() {
        return narrowerSystemTypeID;
    }

    public void setNarrowerSystemTypeID(List<String> narrowerSystemTypeID) {
        this.narrowerSystemTypeID = narrowerSystemTypeID;
    }

    public List<String> getNarrowerRequirementID() {
        return narrowerRequirementID;
    }

    public void setNarrowerRequirementID(List<String> narrowerRequirementID) {
        this.narrowerRequirementID = narrowerRequirementID;
    }
}
