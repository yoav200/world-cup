package com.ab.worldcup.results;

public enum MatchResultType {
    HOME_TEAM_WON, DRAW, AWAY_TEAM_WON;

    public MatchResultType opposite(){
        MatchResultType opposite = this;
        switch(this){
            case HOME_TEAM_WON:
                opposite = AWAY_TEAM_WON;
                break;
            case AWAY_TEAM_WON:
                opposite = HOME_TEAM_WON;
                break;
            case DRAW:
                opposite = DRAW;
                break;

        }
         return opposite;
    }
}
