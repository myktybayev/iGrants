package kz.school.grants.spec_menu.models;

public class OneAddSpec {
    private String number;
    private String code;
    private String title;

    public OneAddSpec() {}

    public OneAddSpec(String number, String code, String title) {
        this.number = number;
        this.code = code;
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


