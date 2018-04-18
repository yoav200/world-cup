'use strict';

angular.module('worldcup').controller('accountCtrl', function ($rootScope, $scope, $http, growl) {

    $scope.accounts = [];

    var getAccounts = function () {
        $http.get("/api/account/").then(function (response) {
            console.log(response);
            $scope.accounts = response.data;
        });
    };


    var init = function () {
        getAccounts();
    };

    init();


});