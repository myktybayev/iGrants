package kz.school.grants.spec_menu.models;

import java.util.HashMap;

public class OneSpec {
    private String title;
    private String maxScore;
    private String minScore;

    public OneSpec() {}

    public OneSpec(String title, String maxScore, String minScore) {
        this.title = title;
        this.maxScore = maxScore;
        this.minScore = minScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }
}


