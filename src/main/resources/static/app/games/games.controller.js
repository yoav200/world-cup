'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches) {

    $rootScope.view = { section : ''};

    $scope.matches = {
        firstStage : undefined,
        secondStage : undefined
    };


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