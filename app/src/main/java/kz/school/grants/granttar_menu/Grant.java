package kz.school.grants.granttar_menu;

public class Grant {

    private String gId;
    private String grant_title;
    private String grant_desc;

    public Grant() {

    }

    public Grant(String gId, String grant_title, String grant_desc) {
        this.gId = gId;
        this.grant_title = grant_title;
        this.grant_desc = grant_desc;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getGrant_title() {
        return grant_title;
    }

    public void setGrant_title(String grant_title) {
        this.grant_title = grant_title;
    }

    public String getGrant_desc() {
        return grant_desc;
    }

    public void setGrant_desc(String grant_desc) {
        this.grant_desc = grant_desc;
    }
}
