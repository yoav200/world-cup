'use strict';

angular.module('worldcup').controller('betsMatchesCtrl', function ($rootScope, $scope, $state, $stateParams, Bets, growl, $uibModal, $log) {

    var matchHomeWon = 'HOME_TEAM';

    var matchAwayWon = 'AWAY_TEAM';

    $scope.options = {
        matchQualifier : [matchHomeWon, matchAwayWon],
        bets : undefined
    };

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

    var selectByMatchId = function(matches, matchId) {
        return  matches.find(function(match) {
            return match.matchId === parseInt(matchId) && match.ready;
        });
    };

    var getMatchData = function() {
        Bets.getMatchesData().then(function (response) {
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

    var getAllBets = function() {
        Bets.getAllBets().then(function (response) {
            $scope.options.bets = response;
            angular.forEach(response, function (bet, index) {
                $scope.betsMap[bet.matchId] = bet;
            });
        });
    };

    // ~ =============== START delete bet dialog ================

    $scope.confirmDeleteBet = function() {
        $scope.modalInstance =  $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title-top',
            ariaDescribedBy: 'modal-body-top',
            templateUrl: 'confirmModalContent.html',
            size: 'sm',
            scope: $scope
        });

        $scope.modalInstance.result.then(function () {
            $log.info('Modal is here');
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.deleteBet = function () {
        $scope.modalInstance.close();
        Bets.deleteUserBet($scope.userBet.betId).then(function() {
            getMatchData();
            growl.info('You\'re bet deleted successfully.',{title: 'Deleted!'});
        });
    };

    $scope.cancel = function () {
        $scope.modalInstance.dismiss('cancel');
    };

    // ~ =============== END delete bet dialog ================

    $scope.postUserBet = function () {
        // set matchQualifier according to goals
        // in case of equals matchQualifier=HomeWon
        if($scope.userBet.homeTeamGoals >= $scope.userBet.awayTeamGoals) {
            $scope.userBet.matchQualifier = matchHomeWon;
        } else if($scope.userBet.homeTeamGoals < $scope.userBet.awayTeamGoals) {
            $scope.userBet.matchQualifier = matchAwayWon;
        }
        Bets.updateBet($scope.userBet.betId, $scope.userBet).then(function (response) {
            getMatchData();
            growl.success('You\'re bet saved successfully.',{title: 'Success!'});
        });
    };


    $scope.onMatchSelected = function (stage) {

        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selected.match.matchId === match.matchId) {

                $scope.selected.bet = $scope.betsMap[match.matchId];

                $scope.userBet = {
                    betId: $scope.selected.bet.id,
                    matchId: $scope.selected.match.matchId,
                    homeTeamCode: $scope.selected.match.homeTeam.code,
                    awayTeamCode: $scope.selected.match.awayTeam.code,
                    homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined,
                    matchQualifier: $scope.selected.match.result ? $scope.selected.match.result.matchQualifier : undefined
                };
            }
        });
    };

    var init = function() {
        $log.info("controller init");
        getMatchData();
        getAllBets();
    };

    init();

});