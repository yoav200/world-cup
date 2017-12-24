'use strict';

/**
 * Main AngularJS Web Application
 */
var app = angular.module('worldcup', ['ui.router', 'ui.router.stateHelper', 'ui.bootstrap', 'ngResource', 'ngAnimate', 'ngSanitize']);


/**
 * Configure the Routes using ui router
 */
app.config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider) {
    // $locationProvider.html5Mode(true);
    // For any unmatched url, redirect to /
    $urlRouterProvider.otherwise("/");

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    $httpProvider.defaults.headers.common["Accept"] = "application/json";
    $httpProvider.defaults.headers.common["Content-Type"] = "application/json";

    // Now set up the states
    $stateProvider.state('site', {
        abstract: true,
        views: {
            'navbar@': {
                templateUrl: 'app/navbar/navbar.view.html',
                controller: 'navbarController'
            }
            // , resolve: {
            //     profile: function (Users) {
            //         return Users.fetchProfile();
            //     }
            // }
        }
    }).state('home', {
        parent: 'site',
        url: "/",
        data: {},
        views: {
            'content@': {
                templateUrl: 'app/home/home.view.html',
                controller: 'homeCtrl'
            }
        }
    }).state('join', {
        parent: 'site',
        url: "/join",
        views: {
            'content@': {
                templateUrl: 'app/login/login.view.html',
                controller: 'loginController'
            }
        }
    });

    // Team
    $stateProvider.state('teams', {
        parent: 'site',
        url: '/teams',
        views: {
            'content@': {
                templateUrl: 'app/teams/view/teams.view.html',
                controller: 'teamsCtrl'
            }
        }
    }).state('teams.groups', {
        url: '/groups',
        templateUrl: 'app/teams/view/teams.groups.html',
        controller: 'teamsCtrl'
    }).state('teams.confederation', {
        url: "/confederation",
        data: {},
        views: {
            '': {
                templateUrl: 'app/teams/view/teams.confederations.html',
                controller: 'teamsCtrl'
            }
        }
    }).state('teams.fifaranking', {
        url: "/ranking",
        views: {
            '': {
                templateUrl: 'app/teams/view/teams.fifaranking.html',
                controller: 'teamsCtrl'
            }
        }
    });

    // Matches
    $stateProvider.state('games', {
        parent: 'site',
        url: "/games",
        views: {
            'content@': {
                templateUrl: 'app/games/view/games.view.html',
                controller: 'gamesCtrl'
            }
        }
    }).state('games.firststage', {
        url: "/first-stage",
        templateUrl: 'app/games/view/games.firststage.html',
        controller: 'gamesCtrl'
        // , resolve: {
        //     firstStageMatches:  function($http){
        //         // $http returns a promise for the url data
        //         return $http({method: 'GET', url: '/match/group'});
        //     }
        // }
    }).state('games.secondstage', {
        url: "/second-stage",
        templateUrl: 'app/games/view/games.secondstage.html',
        controller: 'gamesCtrl'
    });


    // Bets
    $stateProvider.state('bets-mine', {
        parent: 'site',
        url: "/bets/mine",
        views: {
            'content@': {
                templateUrl: 'app/bets/bets.view.html',
                controller: 'betsCtrl'
            }
        }
    }).state('bets-statistics', {
        parent: 'site',
        url: "/bets/statistics",
        views: {
            'content@': {
                templateUrl: 'app/bets/bets.view.html',
                controller: 'betsCtrl'
            }
        }
    });

}).filter('capitalize', function() {
    return function(input) {
        return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    }
}).filter('spaceToUnderscore', function() {
    return function(input) {
        return angular.lowercase(input.replace(/\s+/g, '_'));
    }
}).filter('underscoreToSpace', function () {
    return function (input) {
        return input.replace(/_/g, ' ');
    };
});
