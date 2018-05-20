package com.ab.worldcup.match;

import java.util.ArrayList;
import java.util.List;

public enum Stage {
    GROUP, ROUND_OF_16, QUARTER_FINAL, SEMI_FINAL, THIRD_PLACE, THIRD_PLACE_WINNER, FINAL, WINNER;

    public List<Stage> getNextStage() {
        ArrayList<Stage> next = new ArrayList<>();
        switch (this) {
            case GROUP:
                next.add(ROUND_OF_16);
                break;
            case ROUND_OF_16:
                next.add(QUARTER_FINAL);
                break;
            case QUARTER_FINAL:
                next.add(SEMI_FINAL);
                break;
            case SEMI_FINAL:
                next.add(FINAL);
                next.add(THIRD_PLACE);
                break;
            case THIRD_PLACE:
                next.add(THIRD_PLACE_WINNER);
            case FINAL:
                next.add(WINNER);
        }
        return next;
    }
}
