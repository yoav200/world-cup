'use strict';

angular.module('worldcup').controller('betsGroupsCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Bets) {

    $scope.matchess = [];
    $scope.groups = [];

    $scope.prepareTeamsInGroup  = function(teamsInGroup) {
        for(var i=0;i < teamsInGroup.length;i++) {
            $scope.matches = {firstStage : teamsInGroup[i].matches}
        }
        return teamsInGroup;
    };

    var groupsStanding = function() {
        Bets.getGroupsStanding().then(function(response) {
            $scope.groups = response;
        });
    };

    var init = function() {
        groupsStanding();
    };

    init();
});