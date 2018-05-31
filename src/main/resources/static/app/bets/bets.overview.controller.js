'use strict';

angular.module('worldcup').controller('betsOverviewCtrl', function ($rootScope, $scope, $state, $stateParams, Matches, Bets) {

    $scope.overviewData = {
        match: [],
        qualifier: []
    };

    var getOverviewData = function () {
        Bets.getOverview().then(function (response) {
            angular.forEach(response, function (data, index) {
                if (data.bet.type === 'MATCH') {
                    $scope.overviewData.match.push(data);
                } else if (data.bet.type === 'QUALIFIER') {
                    $scope.overviewData.qualifier.push(data);
                }
            });
        });
    };

    var init = function () {
        getOverviewData();
    };

    init();

});