'use strict';

angular.module('worldcup').controller('betsGamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches, Bets) {

    $rootScope.view = {section: ''};

    $scope.matches = {};

    var parentState = 'bets-games', defaultChildState = '.firststage';
    // If the parent state has been transitioned to, redirect to the default child.
    if ($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    var getMatchData = function() {
        Bets.getMatchesData().then(function (response) {
            $scope.matches = Matches.getMatchesForStage(response.firstStage.concat(response.secondStage));
        });
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

        getMatchData();
    };

    init();

});