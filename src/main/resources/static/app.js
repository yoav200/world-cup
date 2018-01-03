'use strict';

/**
 * Main AngularJS Web Application
 */
var app = angular.module('worldcup', ['ui.router', 'ui.router.stateHelper', 'ui.bootstrap', 'ngResource', 'ngAnimate', 'ngSanitize', 'angular-loading-bar']);


/**
 * Configure the Routes using ui router
 */
app.config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
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
                templateUrl: 'app/navbar/login.view.html'
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
        templateUrl: 'app/teams/view/teams.confederations.html', 
        controller: 'teamsCtrl'
    }).state('teams.fifaranking', {
        url: "/ranking",
        templateUrl: 'app/teams/view/teams.fifaranking.html',
        controller: 'teamsCtrl'
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
    }).state('games.secondstage', {
        url: "/second-stage",
        templateUrl: 'app/games/view/games.secondstage.html',
        controller: 'gamesCtrl'
    });


    // Bets
    $stateProvider.state('bets-mine', {
        parent: 'site',
        permissions: ['ROLE_USER'],
        url: "/bets/mine",
        views: {
            'content@': {
                templateUrl: 'app/bets/bets.view.html',
                controller: 'betsCtrl'
            }
        }
    }).state('bets-statistics', {
        parent: 'site',
        permissions: ['ROLE_USER'],
        url: "/bets/statistics",
        views: {
            'content@': {
                templateUrl: 'app/bets/bets.view.html',
                controller: 'betsCtrl'
            }
        }
    });

    // Admin
    $stateProvider.state('admin', {
        parent: 'site',
        permissions: ['ROLE_ADMIN'],
        url: "/admin",
        views: {
            'content@': {
                templateUrl: 'app/admin/admin.view.html',
                controller: 'adminCtrl'
            }
        }
    })
    
})/*.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
}])*/.filter('capitalize', function () {
    return function (input) {
        return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    }
}).filter('spaceToUnderscore', function () {
    return function (input) {
        return angular.lowercase(input.replace(/\s+/g, '_'));
    }
}).filter('underscoreToSpace', function () {
    return function (input) {
        return input.replace(/_/g, ' ');
    };
}).run(function ($rootScope, $state, Auth) {

    Auth.init();

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if (!Auth.checkPermissionForView(toState)) {
            event.preventDefault();
            $state.go('join');
        }
    });
});
