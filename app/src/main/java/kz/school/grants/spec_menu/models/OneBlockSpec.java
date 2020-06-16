package kz.school.grants.spec_menu.models;

import java.util.HashMap;

public class OneBlockSpec {
    private String title;
    private HashMap<String, String> professions;
    private HashMap<String, Object> grant18_19;
    private HashMap<String, Object> grant19_20;
    private HashMap<String, GrantCounts> jalpiEnt;

    public OneBlockSpec() {}

    public OneBlockSpec(String title,
                        HashMap<String, String> professions,
                        HashMap<String, Object> grant18_19,
                        HashMap<String, Object> grant19_20,
                        HashMap<String, GrantCounts> jalpiEnt) {

        this.title = title;
        this.professions = professions;
        this.grant18_19 = grant18_19;
        this.grant19_20 = grant19_20;
        this.jalpiEnt = jalpiEnt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, String> getProfessions() {
        return professions;
    }

    public void setProfessions(HashMap<String, String> professions) {
        this.professions = professions;
    }

    public HashMap<String, Object> getGrant18_19() {
        return grant18_19;
    }

    public void setGrant18_19(HashMap<String, Object> grant18_19) {
        this.grant18_19 = grant18_19;
    }

    public HashMap<String, Object> getGrant19_20() {
        return grant19_20;
    }

    public void setGrant19_20(HashMap<String, Object> grant19_20) {
        this.grant19_20 = grant19_20;
    }

    public HashMap<String, GrantCounts> getJalpiEnt() {
        return jalpiEnt;
    }

    public void setJalpiEnt(HashMap<String, GrantCounts> jalpiEnt) {
        this.jalpiEnt = jalpiEnt;
    }
}


