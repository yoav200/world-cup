'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches) {

    $rootScope.view = {section: ''};

    $scope.matches = {
        firstStage: [],
        secondStage: []
    };

    var parentState = 'games', defaultChildState = '.firststage';
    // If the parent state has been transitioned to, redirect to the default child.
    if ($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    Matches.getAllMatches().then(function (response) {
        for (var i = 0; i < response.length; i++) {
            var match = response[i];
            if (match.stage.startsWith("GROUP")) {
                $scope.matches.firstStage.push(match);
            } else {
                $scope.matches.secondStage.push(match);
            }
        }
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