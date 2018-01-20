'use strict';

angular.module('worldcup').controller('betsCtrl', function ($rootScope, $scope, $state, $stateParams, Bets, growl) {

    $scope.matchesData = {};
    $scope.betsMap = {};
    $scope.matchesMap = {};

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

        angular.forEach($scope.matchesData.firstStage , function (match, index) {
            $scope.matchesMap[match.matchId] = match;
        });
        angular.forEach($scope.matchesData.secondStage, function (match, index) {
            $scope.matchesMap[match.matchId] = match;
        });
    });

    Bets.getAllBets().then(function (response) {
        $scope.bets = response;
        angular.forEach(response, function (bet, index) {
            $scope.betsMap[bet.matchId] = bet;
        });
    });

    $scope.postUserBet = function () {
        if($scope.selected.match && $scope.selected.bet
            && $scope.selected.match.matchId === $scope.selected.bet.matchId
            && $scope.userBet.matchId === $scope.selected.bet.matchId
            && $scope.userBet.betId === $scope.selected.bet.id
            && $scope.userBet.homeTeamGoals && $scope.userBet.awayTeamGoals) {

            Bets.updateBet($scope.userBet.betId, $scope.userBet).then(function (response) {
                console.log(response);
                growl.success('You\'re bet saved successfully.',{title: 'Success!'});
            });
        } else {
            growl.warning('You must set home and away teams goals',{title: 'Validation error!!'});
        }
    };

    $scope.onMatchSelected = function (stage) {

        var match = $scope.matchesMap[$scope.selected.match.matchId];

        if(match) {
            $scope.selected.match = match;
            $scope.userBet = {
                matchId: $scope.selected.match.matchId,
                homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined
            };

            var bet = $scope.betsMap[match.matchId];

            if(bet) {
                $scope.selected.bet = bet;
                $scope.userBet.betId = bet.id;
            }
        }
        //console.log($scope.selected);
    };
});