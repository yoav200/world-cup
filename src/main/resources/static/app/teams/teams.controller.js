'use strict';

angular.module('worldcup').controller('teamsCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter) {

    $scope.teams = [];
    $scope.groups = [];
    $scope.confederation = [];

    $http({
        method : "GET",
        url : "teams/"
    }).then(function mySuccess(response) {
        //console.log(response);
        if(response && response.data) {
            $scope.teams = response.data;

            for (var i=0;i<$scope.teams.length;i++) {
                $scope.teams[i].confederation = angular.lowercase($scope.teams[i].confederation.replace(/\s+/g, '_'));
            }

            $scope.teams = $filter('orderBy')($scope.teams, 'fifaRanking');
        }
    }, function error(error) {
        console.log(error);
    });

    console.log($scope.teams)

});