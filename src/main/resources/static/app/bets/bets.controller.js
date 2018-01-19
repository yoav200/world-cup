'use strict';

angular.module('worldcup').controller('betsCtrl', function ($rootScope, $scope, $state, $stateParams, Bets) {

    $scope.matchesData = {};

    $scope.betsData = {
        match: [],
        qualifier: []
    };

    $scope.selected = {
        match: undefined,
        bet: undefined
    };

    $scope.userBet = {
        matchId: undefined,
        betId: undefined,
        label: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        qualifier: undefined
    };

    Bets.getMatchesData().then(function (response) {
        $scope.matchesData = response;
    });

    Bets.getAllBets().then(function (response) {
        $scope.bets = response;

        angular.forEach(response, function (bet, index) {
            if (bet.type === "MATCH") {
                $scope.betsData.match.push(bet);
            } else if (bet.type === "QUALIFIER") {
                $scope.betsData.qualifier.push(bet);
            }
        });
    });

    $scope.postUserBet = function () {
        if($scope.selected.match && $scope.selected.bet
            && $scope.selected.match.matchId === $scope.selected.bet.matchId
            && $scope.userBet.matchId === $scope.selected.bet.matchId
            && $scope.userBet.betId === $scope.selected.bet.id) {
            Bets.updateBet($scope.userBet.betId, $scope.userBet).then(function (response) {
                console.log(response);
            });
        }

    };

    $scope.onMatchSelected = function (stage) {
        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selected.match.matchId === match.matchId) {
                $scope.selected.match = match;
                $scope.userBet = {
                    matchId: $scope.selected.match.matchId,
                    homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined
                };
            }
        });

        angular.forEach($scope.betsData.match, function (bet, index) {
            if ($scope.selected.match.matchId === bet.matchId) {
                $scope.selected.bet = bet;
                $scope.userBet.betId = bet.id;
                return;
            }
        });

        console.log($scope.selected);
    };

});