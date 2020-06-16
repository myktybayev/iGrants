package kz.school.grants.spec_menu.models;

import java.util.HashMap;

public class GrantCounts {
    private Long ave;
    private Long max;
    private Long min;

    public GrantCounts() {}

    public GrantCounts(Long ave, Long max, Long min) {

        this.ave = ave;
        this.max = max;
        this.min = min;
    }

    public Long getAve() {
        return ave;
    }

    public void setAve(Long ave) {
        this.ave = ave;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }
}


