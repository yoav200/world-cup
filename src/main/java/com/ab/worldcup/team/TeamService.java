package com.ab.worldcup.team;

public class TeamService {

    public static final Team UndeterminedTeam = new Team(){

        @Override
        public String getName() {
            return "Not yet determined";
        }
    };
}
