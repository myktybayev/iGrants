package kz.school.grants.advice_menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import kz.school.grants.spec_menu.models.OneBlockSpec;

public class Advice implements Serializable {
    private String adviceId;
    private String studentName;
    private String studentPhone;
    private String adviceDate;
    private ArrayList<String> groupList;
    private ArrayList<String> univerList;

    public Advice() {}

    public Advice(String adviceId, String studentName, String studentPhone, String adviceDate) {
        this.adviceId = adviceId;
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.adviceDate =  adviceDate;
    }

    public Advice(String adviceId, String studentName,String studentPhone, String adviceDate, ArrayList<String> groupList, ArrayList<String> univerList) {
        this.adviceId = adviceId;
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.adviceDate =  adviceDate;
        this.groupList =  groupList;
        this.univerList =  univerList;
    }

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getAdviceDate() {
        return adviceDate;
    }

    public void setAdviceDate(String adviceDate) {
        this.adviceDate = adviceDate;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<String> getUniverList() {
        return univerList;
    }

    public void setUniverList(ArrayList<String> univerList) {
        this.univerList = univerList;
    }
}


