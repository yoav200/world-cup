'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches) {

    $rootScope.view = {
        section: ''
    };

    $scope.matches = [];

    var parentState = 'games', defaultChildState = '.firststage';
    // If the parent state has been transitioned to, redirect to the default child.
    if ($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    Matches.getMatchesData().then(function (response) {
        $scope.matches = Matches.getMatchesForStage(response.firstStage.concat(response.secondStage));
    });

    var init = function () {
        var viewName = '';
        switch ($state.current.name) {
            case 'games.firststage' :
                viewName = "First Stage";
                break;
            case 'games.secondstage' :
                viewName = "Second Stage";
                break;
        }
        $rootScope.view = {section: viewName};
    };

    init();

});