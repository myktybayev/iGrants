package kz.school.grants.granttar_menu.models;

import java.util.HashMap;

public class SpecItem {
    private String specCode;
    private String specName;
    private String specSubjectPair;
    private HashMap<String, OneUniver> univers;

    public SpecItem() {}

    public SpecItem(String specCode, String specName, String specSubjectPair, HashMap<String, OneUniver> univers) {
        this.specCode = specCode;
        this.specName = specName;
        this.specSubjectPair = specSubjectPair;
        this.univers = univers;
    }

    public SpecItem(String specCode, String specName, String specSubjectPair) {
        this.specCode = specCode;
        this.specName = specName;
        this.specSubjectPair = specSubjectPair;
    }

    public HashMap<String, OneUniver> getUnivers() {
        return univers;
    }

    public void setUnivers(HashMap<String, OneUniver> univers) {
        this.univers = univers;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecSubjectPair() {
        return specSubjectPair;
    }

    public void setSpecSubjectPair(String specSubjectPair) {
        this.specSubjectPair = specSubjectPair;
    }
}


