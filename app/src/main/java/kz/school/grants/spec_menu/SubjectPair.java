package kz.school.grants.spec_menu;

public class SubjectPair {
    String sId;
    String pair;
    int count;

    public SubjectPair() {
    }

    public SubjectPair(String sId, String pair, int count) {
        this.sId = sId;
        this.pair = pair;
        this.count = count;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


