package kz.school.grants.granttar_menu.models;

import java.util.HashMap;

import kz.school.grants.spec_menu.models.GrantCounts;

public class OneUniver {
    private String univerCode;
    private HashMap<String, Object> grant18_19;
    private HashMap<String, Object> grant19_20;
    private HashMap<String, GrantCounts> jalpiEnt;

    public OneUniver() {}

    public OneUniver(String univerCode,
                     HashMap<String, Object> grant18_19,
                     HashMap<String, Object> grant19_20,
                     HashMap<String, GrantCounts> jalpiEnt) {

        this.univerCode = univerCode;
        this.grant18_19 = grant18_19;
        this.grant19_20 = grant19_20;
        this.jalpiEnt = jalpiEnt;
    }

    public String getUniverCode() {
        return univerCode;
    }

    public void setUniverCode(String univerCode) {
        this.univerCode = univerCode;
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


