package model;

import java.util.List;

public class Requirement {

    private String requirementID;
    private String label;
    private String language;
    private String prefLabel;
    private String altLabel;
    private String problem;
    private String context;
    private String template;
    private String example;
    private String broaderDomainID;
    private String broaderSystemTypeID;
    private String broaderRequirementTypeID;
    private List<String> narrowerRequirementTypeID;

    public Requirement() {
    }

    public String getRequirementID() {
        return requirementID;
    }

    public void setRequirementID(String requirementID) {
        this.requirementID = requirementID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getBroaderDomainID() {
        return broaderDomainID;
    }

    public void setBroaderDomainID(String broaderDomainID) {
        this.broaderDomainID = broaderDomainID;
    }

    public String getBroaderSystemTypeID() {
        return broaderSystemTypeID;
    }

    public void setBroaderSystemTypeID(String broaderSystemTypeID) {
        this.broaderSystemTypeID = broaderSystemTypeID;
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
}
