'use strict';

/**
 * Main AngularJS Web Application
 */
var app = angular.module('worldcup', ['ui.router', 'ui.bootstrap', 'ngResource',  'ngAnimate', 'ngSanitize']);


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
        'abstract': true,
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
    }).state('teams', {
        parent: 'site',
        url: "/teams",
        data: {},
        views: {
            'content@': {
                templateUrl: 'app/teams/teams.view.html',
                controller: 'teamsCtrl'
            }
        }
    }).state('games', {
        parent: 'site',
        url: "/games",
        views: {
            'content@': {
                templateUrl: 'app/games/games.view.html',
                controller: 'gamesCtrl'
            }
        }
    }).state('bets-mine', {
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
});
