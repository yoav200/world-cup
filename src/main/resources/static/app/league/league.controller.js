'use strict';

angular.module('worldcup').controller('leagueCtrl', function ($scope, $rootScope, Leagues, growl) {

    $scope.leagues = [];
    $scope.selectedLeague = null;
    $scope.members = [];
    $scope.newLeagueName = '';
    $scope.newMemberEmail = '';
    $scope.canCreate = true;

    var loadLeagues = function () {
        Leagues.getCreatedLeagues().then(function (leagues) {
            $scope.leagues = leagues;
            $scope.canCreate = leagues.length < 2;
            if ($scope.selectedLeague) {
                // Refresh selected league reference
                var found = leagues.find(function(l) { return l.id === $scope.selectedLeague.id; });
                if (found) {
                    $scope.selectedLeague = found;
                } else {
                    $scope.selectedLeague = null;
                    $scope.members = [];
                }
            }
        });
    };

    $scope.createLeague = function () {
        if (!$scope.newLeagueName || !$scope.newLeagueName.trim()) return;
        Leagues.createLeague($scope.newLeagueName.trim()).then(function (league) {
            $scope.newLeagueName = '';
            growl.success('League "' + league.name + '" created!');
            loadLeagues();
            $scope.selectLeague(league);
        }, function () {
            growl.error('Failed to create league. You can create max 2 leagues.');
        });
    };

    $scope.selectLeague = function (league) {
        $scope.selectedLeague = league;
        $scope.loadMembers();
    };

    $scope.loadMembers = function () {
        if (!$scope.selectedLeague) return;
        Leagues.getMembers($scope.selectedLeague.id).then(function (members) {
            $scope.members = members;
        });
    };

    $scope.addMember = function () {
        if (!$scope.newMemberEmail || !$scope.newMemberEmail.trim()) return;
        if (!$scope.selectedLeague) return;
        Leagues.addMember($scope.selectedLeague.id, $scope.newMemberEmail.trim()).then(function () {
            $scope.newMemberEmail = '';
            growl.success('Member added');
            $scope.loadMembers();
        }, function () {
            growl.error('Failed to add member');
        });
    };

    $scope.removeMember = function (membership) {
        if (!$scope.selectedLeague) return;
        Leagues.removeMember($scope.selectedLeague.id, membership.id).then(function () {
            growl.success('Member removed');
            $scope.loadMembers();
        }, function () {
            growl.error('Cannot remove this member');
        });
    };

    $scope.deleteLeague = function () {
        if (!$scope.selectedLeague) return;
        if (!confirm('Delete league "' + $scope.selectedLeague.name + '"? This cannot be undone.')) return;
        Leagues.deleteLeague($scope.selectedLeague.id).then(function () {
            growl.success('League deleted');
            $scope.selectedLeague = null;
            $scope.members = [];
            loadLeagues();
        });
    };

    $scope.updateLeagueName = function () {
        if (!$scope.selectedLeague || !$scope.selectedLeague.name.trim()) return;
        Leagues.updateLeague($scope.selectedLeague.id, $scope.selectedLeague.name.trim()).then(function () {
            growl.success('League name updated');
            loadLeagues();
        });
    };

    $scope.isCreator = function (membership) {
        if (!$scope.selectedLeague) return false;
        return membership.email === $scope.selectedLeague.createdBy.email;
    };

    // Init
    loadLeagues();
});
