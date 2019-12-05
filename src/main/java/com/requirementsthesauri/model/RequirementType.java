package com.requirementsthesauri.model;

import java.util.List;

public class RequirementType {

    private String requirementTypeID;
    private String label;
    private String prefLabel;
    private String altLabel;
    private String description;
    private String broaderRequirementTypeID;
    private List<String> narrowerRequirementTypeID;
    private List<String> narrowerRequirementID;

    public RequirementType() {
    }

    public String getRequirementTypeID() {
        return requirementTypeID;
    }

    public void setRequirementTypeID(String requirementTypeID) {
        this.requirementTypeID = requirementTypeID;
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

    public String getBroaderRequirementTypeID() {
        return broaderRequirementTypeID;
    }

    public void setBroaderRequirementTypeID(String broaderRequirementTypeID) {
        this.broaderRequirementTypeID = broaderRequirementTypeID;
    }

    public List<String> getNarrowerRequirementTypeID() {
        return narrowerRequirementTypeID;
    }

    public void setNarrowerRequirementTypeID(List<String> narrowerRequirementTypeID) {
        this.narrowerRequirementTypeID = narrowerRequirementTypeID;
    }

    public List<String> getNarrowerRequirementID() {
        return narrowerRequirementID;
    }

    public void setNarrowerRequirementID(List<String> narrowerRequirementID) {
        this.narrowerRequirementID = narrowerRequirementID;
    }

}
