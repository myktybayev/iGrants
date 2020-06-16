package kz.school.grants.univer_menu.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Profession implements Serializable {

    private String blockCode;
    private String subjectPair;
    private String profCode;
    private String profTitle;

    public Profession() {}

    public Profession(String blockCode, String subjectPair, String profCode, String profTitle) {
        this.blockCode = blockCode;
        this.subjectPair = subjectPair;
        this.profCode = profCode;
        this.profTitle = profTitle;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getSubjectPair() {
        return subjectPair;
    }

    public void setSubjectPair(String subjectPair) {
        this.subjectPair = subjectPair;
    }

    public String getProfCode() {
        return profCode;
    }

    public void setProfCode(String profCode) {
        this.profCode = profCode;
    }

    public String getProfTitle() {
        return profTitle;
    }

    public void setProfTitle(String profTitle) {
        this.profTitle = profTitle;
    }
}
