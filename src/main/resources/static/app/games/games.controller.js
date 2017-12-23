'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches) {

    $rootScope.view = { section : ''};

    $scope.matches = {
        firstStage : undefined,
        secondStage : undefined
    };


    var parentState = 'games',
        defaultChildState = '.firststage';
    // If the parent state has been transitioned to, redirect to the default child.
    if($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    Matches.getFirstStageMatches().then(function(response){
        $scope.matches.firstStage = response;
    });

    Matches.getSecondStageMatches().then(function(response){
        $scope.matches.secondStage = response;
    });


    var init = function() {
        var viewName = '';
        switch ($state.current.name) {
            case 'games.firststage' : viewName= "First Stage"; break;
            case 'games.secondstage' : viewName= "Second Stage"; break;
        }

        $rootScope.view = { section : viewName};
    };

    init();

});