'use strict';

angular.module('worldcup').controller('betsMatchesCtrl', function ($rootScope, $scope, $state, $stateParams, Bets, growl, $uibModal, $log) {

    $scope.data = {
        // all matches data
        matches: {},
        // holds a map of matchId to map
        bets: {}
    };
    // selected data by user
    $scope.selected = {
        match: undefined,
        bet: undefined
    };
    // model that holds data
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
    // statistics popover
    $scope.infoPopover = {
        templateUrl: 'infoPopoverTemplate.html',
        title: 'Bet Statistics',
        betsOnHomeTeamPercent: undefined,
        betsOnDrawPercent: undefined,
        betsOnAwayTeamPercent: undefined
    };

    var selectByMatchId = function (matchId) {
        var matches = $scope.data.matches.firstStage.concat($scope.data.matches.secondStage);
        return matches.find(function (match) {
            return match.matchId === parseInt(matchId) && match.ready;
        });
    };

    var getMatchData = function (matchId) {
        Bets.getMatchesData().then(function (response) {
            $scope.data.matches = response;

            if (matchId) {
                var match = selectByMatchId(matchId);

                if (match) {
                    $scope.selected.match = match;
                    $scope.onMatchSelected('');
                } else {
                    growl.info('Match in second stage and not set yet or wrong match id', {title: 'Match not found!'});
                }
            }
        });
    };

    // ~ =============== START delete bet dialog ================

    $scope.confirmDeleteBet = function () {
        $scope.modalInstance = $uibModal.open({
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
        Bets.deleteUserBet($scope.userBet.betId).then(function () {
            getMatchData();
            growl.info('You\'re bet deleted successfully.', {title: 'Deleted!'});
        });
    };

    $scope.cancel = function () {
        $scope.modalInstance.dismiss('cancel');
    };

    // ~ =============== END delete bet dialog ================

    $scope.postUserBet = function () {
        // set matchQualifier according to goals
        // in case of equals matchQualifier=HomeWon
        if ($scope.userBet.homeTeamGoals >= $scope.userBet.awayTeamGoals) {
            $scope.userBet.matchQualifier = 'HOME_TEAM';
        } else if ($scope.userBet.homeTeamGoals < $scope.userBet.awayTeamGoals) {
            $scope.userBet.matchQualifier = 'AWAY_TEAM';
        }
        Bets.updateBet($scope.userBet.betId, $scope.userBet).then(function (response) {
            getMatchData($scope.selected.match.matchId);
            growl.success('You\'re bet saved successfully.', {title: 'Success!'});
        });
    };

    $scope.scrollMatches = function (prevNext) {
        if (!$scope.selected.match) {
            return;
        }
        var matchId = (prevNext === 'next') ? $scope.selected.match.matchId + 1 : $scope.selected.match.matchId - 1;

        var match = selectByMatchId(matchId);

        if (!match) {
            match = selectByMatchId(1);
        }

        $scope.selected.match = match;
        $scope.onMatchSelected('');
    };


    $scope.onMatchSelected = function (stage) {
        var list = $scope.data.matches.firstStage.concat($scope.data.matches.secondStage);
        angular.forEach(list, function (match, index) {
            if ($scope.selected.match.matchId === match.matchId) {
                // set selected bet
                $scope.selected.bet = $scope.data.bets[match.matchId];
                // set model
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

        Bets.getBetStatistics($scope.selected.bet.id).then(function (response) {
            $scope.infoPopover = {
                templateUrl: 'infoPopoverTemplate.html',
                title: 'Bet Statistics',
                betsOnHomeTeamPercent: response.betsOnHomeTeamPercent,
                betsOnDrawPercent: response.betsOnDrawPercent,
                betsOnAwayTeamPercent: response.betsOnAwayTeamPercent
            };
        });
    };

    var init = function () {
        // load all bets
        Bets.getAllBets().then(function (response) {
            angular.forEach(response, function (bet, index) {
                $scope.data.bets[bet.matchId] = bet;
            });
            // load match data
            getMatchData($stateParams.matchId);
        });
    };

    init();

});