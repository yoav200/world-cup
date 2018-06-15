'use strict';

angular.module('worldcup').controller('accountCtrl', function ($rootScope, $scope, $http, growl) {

    $scope.accounts = [];

    $scope.account = {
        status: undefined
    };

    $scope.statuses = [
        {value: 'REGISTER', text: 'REGISTER'},
        {value: 'ACTIVE', text: 'ACTIVE'}
    ];

    $scope.showStatus = function (status) {
        return (status) ? status : 'Not set';
    };

    var getAccounts = function () {
        $http.get("/api/account/").then(function (response) {
            $scope.accounts = response.data;
        });
    };

    $scope.updateUser = function (data, id) {
        var account = {id: id, status: data};
        return $http.post('/api/account/' + id, account).then(function () {
            growl.success('Account status updated', {title: 'Success!'});
        }, function () {
            //
        });
    };


    var init = function () {
        getAccounts();
    };

    init();


});