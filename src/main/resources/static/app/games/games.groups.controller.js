'use strict';

angular.module('worldcup').controller('groupsCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Teams) {

    $scope.matchess = [];
    $scope.groups = [];

    $scope.prepareTeamsInGroup  = function(teamsInGroup) {
        for(var i=0;i < teamsInGroup.length;i++) {
            $scope.matches = {firstStage : teamsInGroup[i].matches}
        }
        return teamsInGroup;
    };

    var groupsStanding = function() {
        Teams.getGroupsStanding().then(function(response) {
            $scope.groups = response;
        });
    };

    var init = function() {
        groupsStanding();
    };

    init();
});