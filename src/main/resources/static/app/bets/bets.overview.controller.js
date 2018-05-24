'use strict';

angular.module('worldcup').controller('betsOverviewCtrl', function ($rootScope, $scope, $state, $stateParams, $log, Matches, Bets) {


    $scope.overviewData = {
        match: []
    };

    var getOverviewData = function() {
        Bets.getOverview().then(function (response) {

            angular.forEach(response, function (data, index) {
                $scope.overviewData.match.push(data);
            });
        });
    };


    var init = function () {
        $log.info("betsOverviewCtrl init");
        getOverviewData();
    };

    init();

});