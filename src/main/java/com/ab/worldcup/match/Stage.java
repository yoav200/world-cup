package com.ab.worldcup.match;

public enum Stage {
    GROUP,ROUND_OF_16,QUARTER_FINAL,SEMI_FINAL,THIRD_PLACE,FINAL,WINNER;

    public Stage getNextStage(){
        Stage next = this;
        switch(this){
            case GROUP:
                next = ROUND_OF_16;
                break;
            case ROUND_OF_16:
                next = QUARTER_FINAL;
                break;
            case QUARTER_FINAL:
                next = SEMI_FINAL;
                break;
            case SEMI_FINAL:
                next = FINAL;
                break;
            case FINAL:
                next = WINNER;
        }
        return next;
    }
}
