package kz.school.grants.granttar_menu.models;

import kz.school.grants.univer_menu.models.Univer;

public class OneBallEseptuUniver {
    private Univer univer;
    private String maxScore;
    private String minScore;

    public OneBallEseptuUniver() {}

    public OneBallEseptuUniver(Univer univer, String maxScore, String minScore) {
        this.univer = univer;
        this.maxScore = maxScore;
        this.minScore = minScore;
    }

    public Univer getUniver() {
        return univer;
    }

    public void setUniver(Univer univer) {
        this.univer = univer;
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


