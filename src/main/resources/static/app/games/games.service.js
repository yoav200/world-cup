'use strict';

angular.module('worldcup').factory('Matches', function($http) {

    var getMatchesData = function() {
        return $http.get("api/match/data/").then(function(response) {
            return  response.data;
        });
    };

    // var getFirstStageMatches = function() {
    //     return $http.get("api/match/groups").then(function(response) {
    //         return  response.data;
    //     });
    // };

    // var getSecondStageMatches = function() {
    //     return $http.get("api/match/knockout").then(function(response) {
    //         return  response.data;
    //     });
    // };

    var updateMatchResults = function(result) {
        return $http.post("api/match/" + result.matchId, result).then(function(response) {
            return  response.data;
        });
    };


    /**
     * build a map where key is stageId and value is list of matches for the stage
     * @param matches
     * @returns {{firstStage: Array, secondStage: Array, roundOf16Left: Array, roundOf16Right: Array, quarterFinalLeft: Array, quarterFinalRight: Array, semiFinalsLeft: Array, semiFinalsRight: Array, thirdPlace: Array, finals: Array, finalWinner: undefined}}
     */
    var getMatchesForStage = function (matches) {

        var matchesByStage = {
            firstStage: [],
            //secondStage: [],
            roundOf16Left: [],
            roundOf16Right: [],
            quarterFinalLeft: [],
            quarterFinalRight: [],
            semiFinalsLeft: [],
            semiFinalsRight: [],
            thirdPlace: [],
            finals: [],
            finalWinner: undefined
        };
        
        for (var i = 0; i < matches.length; i++) {
            var match = matches[i];
            var matchId = match.matchId;
            var stageId = match.stageId;

            if (stageId === 'GROUP') {
                matchesByStage.firstStage.push(match);
            } else if (stageId === 'ROUND_OF_16') {
                if ([49, 50, 53, 54].indexOf(matchId) > -1) {
                    matchesByStage.roundOf16Left.push(match);
                } else if ([51, 52, 55, 56].indexOf(matchId) > -1) {
                    matchesByStage.roundOf16Right.push(match);
                }
            } else if (stageId === 'QUARTER_FINAL') {
                if ([57, 58].indexOf(matchId) > -1) {
                    matchesByStage.quarterFinalLeft.push(match);
                } else if ([59, 60].indexOf(matchId) > -1) {
                    matchesByStage.quarterFinalRight.push(match);
                }
            } else if (stageId === 'SEMI_FINAL') {
                if ([61].indexOf(matchId) > -1) {
                    matchesByStage.semiFinalsLeft.push(match);
                } else if ([62].indexOf(matchId) > -1) {
                    matchesByStage.semiFinalsRight.push(match);
                }
            } else if (stageId === 'THIRD_PLACE') {
                matchesByStage.thirdPlace.push(match);
            } else if (stageId === 'FINAL') {
                matchesByStage.finals.push(match);
                if(match.result && match.result.winner === 'HOME_TEAM_WON') {
                    matchesByStage.finalWinner = match.homeTeam;
                } else if(match.result && match.result.winner === 'AWAY_TEAM_WON') {
                    matchesByStage.finalWinner = match.awayTeam;
                }
            }
        }
        return matchesByStage;
    };
    
    return {
        getMatchesData: getMatchesData,
        //getFirstStageMatches: getFirstStageMatches,
        //getSecondStageMatches: getSecondStageMatches,
        updateStageMatch: updateMatchResults,
        getMatchesForStage : getMatchesForStage
    };

});