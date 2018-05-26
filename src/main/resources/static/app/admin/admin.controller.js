'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, growl, Matches) {

    var matchHomeWon = 'HOME_TEAM';

    var matchAwayWon = 'AWAY_TEAM';

    $scope.options = {
        matchQualifier : [matchHomeWon, matchAwayWon]
    };
    
    $scope.matchesData = {};

    $scope.selected = {
        match: undefined
    };

    $scope.matchResult = {
        matchId: undefined,
        label: undefined,
        homeTeamCode: undefined,
        awayTeamCode: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        matchQualifier: undefined
    };

    var selectByMatchId = function(matches, matchId) {
        return  matches.find(function(match) {
            return match.matchId === parseInt(matchId) && match.ready;
        });
    };

    var getMatchData = function() {
        Matches.getMatchesData().then(function (response) {
            $scope.matchesData = response;
            // set from parameter
            if($stateParams.matchId) {
                var match;
                var stage;
                if($stateParams.matchId < 49) {
                    match = selectByMatchId(response.firstStage, $stateParams.matchId);
                    stage = 'first';
                } else {
                    match = selectByMatchId(response.secondStage, $stateParams.matchId);
                    stage = 'second';
                }

                if(match) {
                    $scope.selected.match = match;
                    $scope.onMatchSelected(stage);
                } else {
                    growl.info('Match in second stage and not set yet or wrong match id',{title: 'Match not found!'});
                }
            }

        });
    };

    $scope.postResult = function () {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            getMatchData();
            growl.success('Game result saved successfully.',{title: 'Game result saved!'});
        });
    };

    $scope.onGoalsChanged = function() {
        var q = undefined;
        if($scope.matchResult.homeTeamGoals > $scope.matchResult.awayTeamGoals) {
            q = matchHomeWon;
        } else if($scope.matchResult.homeTeamGoals < $scope.matchResult.awayTeamGoals) {
            q = matchAwayWon;
        }
        $scope.matchResult.matchQualifier = q;
    };
    
    $scope.onMatchSelected = function (stage) {
        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selected.match.matchId === match.matchId) {
                $scope.matchResult = {
                    matchId: $scope.selected.match.matchId,
                    homeTeamCode: $scope.selected.match.homeTeam.code,
                    awayTeamCode: $scope.selected.match.awayTeam.code,
                    homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined,
                    //matchQualifier: $scope.selected.match.result ? $scope.selected.match.result.matchQualifier : undefined
                };
                $scope.onGoalsChanged();
            }
        });
    };


    var init = function() {
        getMatchData();
    };

    init();


});