'use strict';

angular.module('worldcup').controller('betsGamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Bets) {

    $rootScope.view = {section: ''};

    $scope.matches = {
        firstStage: [],
        secondStage: [],
        roundOf16Left: [],
        roundOf16Right: [],
        quarterFinalLeft: [],
        quarterFinalRight: [],
        semiFinalsLeft: [],
        semiFinalsRight: [],
        thirdPlace: [],
        finals: []
    };

    $scope.finalWinner = undefined;

    var parentState = 'bets-games', defaultChildState = '.firststage';
    // If the parent state has been transitioned to, redirect to the default child.
    if ($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }


    Bets.getMatchesData().then(function (response) {
        $scope.matches.firstStage = response.firstStage;
        prepareBrackets(response.secondStage);
    });

    var prepareBrackets = function (matches) {
        for (var i = 0; i < matches.length; i++) {
            var match = matches[i];
            if (match.stageId === 'ROUND_OF_16') {
                if ([49, 50, 53, 54].indexOf(match.matchId) > -1) {
                    $scope.matches.roundOf16Left.push(match);
                } else if ([51, 52, 55, 56].indexOf(match.matchId) > -1) {
                    $scope.matches.roundOf16Right.push(match);
                }
            } else if (match.stageId === 'QUARTER_FINAL') {
                if ([57, 58].indexOf(match.matchId) > -1) {
                    $scope.matches.quarterFinalLeft.push(match);
                } else if ([59, 60].indexOf(match.matchId) > -1) {
                    $scope.matches.quarterFinalRight.push(match);
                }
            } else if (match.stageId === 'SEMI_FINAL') {
                if ([61].indexOf(match.matchId) > -1) {
                    $scope.matches.semiFinalsLeft.push(match);
                } else if ([62].indexOf(match.matchId) > -1) {
                    $scope.matches.semiFinalsRight.push(match);
                }
            } else if (match.stageId === 'THIRD_PLACE') {
                $scope.matches.thirdPlace.push(match);
            } else if (match.stageId === 'FINAL') {
                $scope.matches.finals.push(match);
                if(match.results && match.results.winner === 'HOME_TEAM_WON') {
                    $scope.finalWinner = match.homeTeam;
                } else if(match.results && match.results.winner === 'AWAY_TEAM_WON') {
                    $scope.finalWinner = match.awayTeam;
                }
            }
        }
        console.log($scope.matches);
    };

    var init = function () {
        var viewName = '';
        switch ($state.current.name) {
            case 'bets-games.firststage' :
                viewName = "First Stage";
                break;
            case 'bets-games.secondstage' :
                viewName = "Second Stage";
                break;
        }
        $rootScope.view = {section: viewName};
    };

    init();

});