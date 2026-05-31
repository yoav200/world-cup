'use strict';

angular.module('worldcup').controller('navbarController', function ($rootScope, $scope, $state, $http, $interval, Auth) {

    $scope.logout = Auth.logout;
    $scope.coinBucket = null;

    var loadBucket = function () {
        $http.get('api/coins/bucket').then(function (response) {
            $scope.coinBucket = response.data.total;
        });
    };

    // Load on init and refresh every 60 seconds
    loadBucket();
    var bucketRefresh = $interval(loadBucket, 60000);
    $scope.$on('$destroy', function () { $interval.cancel(bucketRefresh); });

});