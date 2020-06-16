package kz.school.grants.spec_menu.models;

import java.util.HashMap;

public class SubjectPair {
    private String id;
    private String pair;
    private int count;
    private HashMap<String, OneBlockSpec> blocks;

    public SubjectPair() {}

    public SubjectPair(String id, String pair, int count, HashMap<String, OneBlockSpec> blocks) {
        this.id = id;
        this.pair = pair;
        this.count = count;
        this.blocks = blocks;
    }

    public HashMap<String, OneBlockSpec> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<String, OneBlockSpec> blocks) {
        this.blocks = blocks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


