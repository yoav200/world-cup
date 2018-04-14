'use strict';

angular.module('worldcup').controller('betsCtrl', function ($rootScope, $scope, $state, $stateParams, Bets, growl) {

    $scope.matchesData = {};

    $scope.betsMap = {};

    $scope.selected = {
        match: undefined,
        bet: undefined
    };

    $scope.userBet = {
        matchId: undefined,
        betId: undefined,
        label: undefined,
        homeTeamCode: undefined,
        awayTeamCode: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        matchQualifier: undefined
    };

    var getMatchData = function() {
        Bets.getMatchesData().then(function (response) {
            $scope.matchesData = response;
        });
    };

    var getAllBets = function() {
        Bets.getAllBets().then(function (response) {
            $scope.bets = response;

            angular.forEach(response, function (bet, index) {
                $scope.betsMap[bet.matchId] = bet;
            });
        });
    };

    $scope.postUserBet = function () {
        Bets.updateBet($scope.userBet.betId, $scope.userBet).then(function (response) {
            getMatchData();
            growl.success('You\'re bet saved successfully.',{title: 'Success!'});
        });
    };

    $scope.onMatchSelected = function (stage) {

        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selected.match.matchId === match.matchId) {
                //$scope.selected.match = match;

                var bet = $scope.betsMap[match.matchId];

                $scope.selected.bet = bet;

                $scope.userBet = {
                    betId: bet.id,
                    matchId: $scope.selected.match.matchId,
                    homeTeamCode: $scope.selected.match.homeTeam.code,
                    awayTeamCode: $scope.selected.match.awayTeam.code,
                    homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined,
                    matchQualifier: $scope.selected.match.result ? $scope.selected.match.result.matchQualifier : undefined
                };
                console.log($scope.userBet);
                console.log($scope.selected);
            }
        });
    };


    var init = function() {
        getMatchData();
        getAllBets();
    };

    init();


});