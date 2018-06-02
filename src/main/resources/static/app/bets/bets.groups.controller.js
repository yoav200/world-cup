'use strict';

angular.module('worldcup').controller('betsGroupsCtrl', function ($rootScope, $scope, $state, Bets) {

    $scope.matchess = [];
    $scope.groups = [];

    $scope.prepareTeamsInGroup  = function(teamsInGroup) {
        for(var i=0;i < teamsInGroup.length;i++) {
            $scope.matches = {firstStage : teamsInGroup[i].matches}
        }
        return teamsInGroup;
    };

    var groupsStanding = function() {
        Bets.getBetsGroupStandings().then(function(response) {
            $scope.groups = response;
        });
    };

    var init = function() {
        groupsStanding();
    };

    init();
});