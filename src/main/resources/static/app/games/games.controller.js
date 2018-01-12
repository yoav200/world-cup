'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams, $http, Matches) {

    $rootScope.view = {section: ''};

    $scope.matches = {
        firstStage: [],
        secondStage: [],
        roundOf16Left: [],
        roundOf16Right: [],
        stages : {}
    };

    $scope.filterByGenres = function(movie) {
        return ($scope.selectedGenres.indexOf(movie.genre) !== -1);
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

            if(match.stage === 'ROUND_OF_16') {
                if([49,50,53,54].indexOf(match.matchId) > -1) {
                    $scope.matches.roundOf16Left.push(match);
                } else if([51,52,55,56].indexOf(match.matchId) > -1) {
                    $scope.matches.roundOf16Right.push(match);
                }
            }

            if (!(match.stage in $scope.matches.stages)) {
                $scope.matches.stages[match.stage] = [];
            }
            $scope.matches.stages[match.stage].push(match);
        }
        console.log($scope.matches);
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