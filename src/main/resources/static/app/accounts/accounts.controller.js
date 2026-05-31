'use strict';

angular.module('worldcup').controller('accountCtrl', function ($rootScope, $scope, $http) {

    $scope.accounts = [];
    $scope.searchQuery = '';
    $scope.currentPage = 1;
    $scope.pageSize = 20;
    $scope.Math = Math;

    $scope.currentAccountId = $rootScope.Account ? $rootScope.Account.id : null;

    $scope.searchFilter = function (account) {
        if (!$scope.searchQuery) return true;
        var q = $scope.searchQuery.toLowerCase();
        return (account.fullName && account.fullName.toLowerCase().indexOf(q) !== -1) ||
               (account.email && account.email.toLowerCase().indexOf(q) !== -1);
    };

    $scope.$watch('searchQuery', function () {
        $scope.currentPage = 1;
    });

    $scope.totalPages = function () {
        var filtered = $scope.accounts.filter($scope.searchFilter);
        return Math.max(1, Math.ceil(filtered.length / $scope.pageSize));
    };

    $scope.pageNumbers = function () {
        var total = $scope.totalPages();
        var pages = [];
        for (var i = 1; i <= total; i++) {
            pages.push(i);
        }
        return pages;
    };

    var getAccounts = function () {
        $http.get("/api/account/").then(function (response) {
            $scope.accounts = response.data;
        });
    };

    var init = function () {
        getAccounts();
    };

    init();

});