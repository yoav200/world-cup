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
     * @returns object with arrays for each bracket position
     */
    var getMatchesForStage = function (matches) {

        var matchesByStage = {
            firstStage: [],
            roundOf32Left: [],
            roundOf32Right: [],
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

        // collect knockout matches by stage
        var stageCollectors = {};

        for (var i = 0; i < matches.length; i++) {
            var match = matches[i];
            var stageId = match.stageId;

            if (stageId === 'GROUP') {
                matchesByStage.firstStage.push(match);
            } else if (stageId === 'THIRD_PLACE') {
                matchesByStage.thirdPlace.push(match);
            } else if (stageId === 'FINAL') {
                matchesByStage.finals.push(match);
                if(match.result && match.result.winner === 'HOME_TEAM_WON') {
                    matchesByStage.finalWinner = match.homeTeam;
                } else if(match.result && match.result.winner === 'AWAY_TEAM_WON') {
                    matchesByStage.finalWinner = match.awayTeam;
                }
            } else {
                if (!stageCollectors[stageId]) {
                    stageCollectors[stageId] = [];
                }
                stageCollectors[stageId].push(match);
            }
        }

        // split each knockout stage into left/right halves by matchId order
        var splitStage = function(stageList) {
            if (!stageList) return { left: [], right: [] };
            stageList.sort(function(a, b) { return a.matchId - b.matchId; });
            var half = Math.ceil(stageList.length / 2);
            return {
                left: stageList.slice(0, half),
                right: stageList.slice(half)
            };
        };

        var r32 = splitStage(stageCollectors['ROUND_OF_32']);
        matchesByStage.roundOf32Left = r32.left;
        matchesByStage.roundOf32Right = r32.right;

        var r16 = splitStage(stageCollectors['ROUND_OF_16']);
        matchesByStage.roundOf16Left = r16.left;
        matchesByStage.roundOf16Right = r16.right;

        var qf = splitStage(stageCollectors['QUARTER_FINAL']);
        matchesByStage.quarterFinalLeft = qf.left;
        matchesByStage.quarterFinalRight = qf.right;

        var sf = splitStage(stageCollectors['SEMI_FINAL']);
        matchesByStage.semiFinalsLeft = sf.left;
        matchesByStage.semiFinalsRight = sf.right;

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