package kz.school.grants.univer_menu.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Univer implements Serializable {

    private String univerId;
    private String univerImage;
    private String univerName;
    private String univerPhone;
    private String univerLocation;
    private String univerCode;
    private String professions;

    public Univer() {}

    public Univer(String univerId,      String univerImage,     String univerName,
                  String univerPhone,   String univerLocation,  String univerCode,
                  String professions) {
        this.univerId = univerId;
        this.univerImage = univerImage;
        this.univerName = univerName;
        this.univerPhone = univerPhone;
        this.univerLocation = univerLocation;
        this.univerCode = univerCode;
        this.professions = professions;
    }

    public Univer(String univerId,      String univerImage,     String univerName,
                  String univerPhone,   String univerLocation,  String univerCode) {
        this.univerId = univerId;
        this.univerImage = univerImage;
        this.univerName = univerName;
        this.univerPhone = univerPhone;
        this.univerLocation = univerLocation;
        this.univerCode = univerCode;
    }

    public String getProfessions() {
        return professions;
    }

    public void setProfessions(String professions) {
        this.professions = professions;
    }

    public String getUniverPhone() {
        return univerPhone;
    }

    public void setUniverPhone(String univerPhone) {
        this.univerPhone = univerPhone;
    }

    public String getUniverId() {
        return univerId;
    }

    public void setUniverId(String univerId) {
        this.univerId = univerId;
    }

    public String getUniverImage() {
        return univerImage;
    }

    public void setUniverImage(String univerImage) {
        this.univerImage = univerImage;
    }

    public String getUniverName() {
        return univerName;
    }

    public void setUniverName(String univerName) {
        this.univerName = univerName;
    }

    public String getUniverLocation() {
        return univerLocation;
    }

    public void setUniverLocation(String univerLocation) {
        this.univerLocation = univerLocation;
    }

    public String getUniverCode() {
        return univerCode;
    }

    public void setUniverCode(String univerCode) {
        this.univerCode = univerCode;
    }
}
