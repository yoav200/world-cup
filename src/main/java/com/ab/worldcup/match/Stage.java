package com.ab.worldcup.match;

import java.util.Arrays;
import java.util.List;

public enum Stage {
    GROUP,ROUND_OF_16,QUARTER_FINAL,SEMI_FINAL,THIRD_PLACE,FINAL,WINNER;

    public List<Stage> getNextStage(){
        List<Stage> next = Arrays.asList(this);
        switch(this){
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
            case FINAL:
                next.add(WINNER);
        }
        return next;
    }
}
