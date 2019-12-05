package com.requirementsthesauri.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@EntityScan
public class Domain {

    private String domainID;
    private String label;
    private String prefLabel;
    private String altLabel;
    private String description;
    private String broaderDomainID;
    private List<String> narrowerDomainID;
    private List<String> narrowerRequirementID;

    public Domain() {
    }

    public String getDomainID() {
        return domainID;
    }

    public void setDomainID(String domainID) {
        this.domainID = domainID;
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

    public String getBroaderDomainID() {
        return broaderDomainID;
    }

    public void setBroaderDomainID(String broaderDomainID) {
        this.broaderDomainID = broaderDomainID;
    }

    public List<String> getNarrowerDomainID() {
        return narrowerDomainID;
    }

    public void setNarrowerDomainID(List<String> narrowerDomainID) {
        this.narrowerDomainID = narrowerDomainID;
    }

    public List<String> getNarrowerRequirementID() {
        return narrowerRequirementID;
    }

    public void setNarrowerRequirementID(List<String> narrowerRequirementID) {
        this.narrowerRequirementID = narrowerRequirementID;
    }
}
