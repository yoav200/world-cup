package com.ab.worldcup.match;

public enum Stage {
    GROUP,ROUND_OF_16,QUARTER_FINAL,SEMI_FINAL,THIRD_PLACE,FINAL,WINNER;

    public Stage getNextStage(Stage current){
        switch(current){
            case GROUP:
                return ROUND_OF_16;
            case ROUND_OF_16:
                return QUARTER_FINAL;
            case QUARTER_FINAL:
                return SEMI_FINAL;
            case SEMI_FINAL:
                return FINAL;
            case FINAL:
                return WINNER;
        }
        return current;
    }
}
