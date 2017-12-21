'use strict';

angular.module('worldcup').controller('gamesCtrl', function ($rootScope, $scope, $state, $stateParams) {

    var init = function() {
        var viewName = '';
        switch ($state.current.name) {
            case 'games.firststage' : viewName= "First Stage"; break;
            case 'games.secondstage' : viewName= "Second Stage"; break;
        }

        $rootScope.view = { section : viewName};
    };

    init();

});