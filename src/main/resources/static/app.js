'use strict';

/**
 * Main AngularJS Web Application
 */
var app = angular.module('worldcup', ['ui.router', 'ui.router.stateHelper', 'ui.bootstrap', 'ui.select', 'ngResource', 'ngAnimate', 'ngSanitize', 'angular-loading-bar', 'angular-growl']);


/**
 * Configure the Routes using ui router
 */
app.config(function ($stateProvider, $urlRouterProvider, $httpProvider, growlProvider) {
    // $locationProvider.html5Mode(true);
    // For any unmatched url, redirect to /
    $urlRouterProvider.otherwise("/");

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    $httpProvider.defaults.headers.common["Accept"] = "application/json";
    $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
    $httpProvider.interceptors.push(growlProvider.serverMessagesInterceptor);

    growlProvider.globalPosition('top-center');
    growlProvider.globalTimeToLive({success: 10000, error: 10000, warning: 10000, info: 10000});

    // Now set up the states
    $stateProvider.state('site', {
        abstract: true,
        views: {
            'navbar@': {
                templateUrl: 'app/navbar/navbar.view.html',
                controller: 'navbarController'
            }
        }
    }).state('ranking', {
        parent: 'site',
        // Ranking - default view
        url: '/',
        views: {
            'content@': {
                templateUrl: 'app/ranking/ranking.view.html',
                controller: 'rankingCtrl'
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

    // Teams
    $stateProvider.state('teams', {
        parent: 'site',
        url: '/teams',
        views: {
            'content@': {
                templateUrl: 'app/teams/view/teams.view.html',
                controller: 'teamsCtrl'
            }
        }
    }).state('teams.confederation', {
        url: "/confederation",
        templateUrl: 'app/teams/view/teams.confederations.html',
        controller: 'teamsCtrl'
    }).state('teams.fifaranking', {
        url: "/ranking",
        templateUrl: 'app/teams/view/teams.fifaranking.html',
        controller: 'teamsCtrl'
    });


    // Games
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
    }).state('games.groups', {
        //parent: 'site',
        url: '/groups',
        views: {
            'content@': {
                templateUrl: 'app/games/view/games.groups.html',
                controller: 'groupsCtrl'
            }
        }
    }).state('games.manage', {
        //parent: 'site',
        permissions: ['ROLE_ADMIN'],
        url: "/manage",
        views: {
            'content@': {
                templateUrl: 'app/admin/admin.view.html',
                controller: 'adminCtrl'
            }
        }
    });

    // Bets
    $stateProvider.state('bets', {
        parent: 'site',
        url: "/bets",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.games.view.html',
                controller: 'betsGamesCtrl'
            }
        }
    }).state('bets.firststage', {
        permissions: ['ROLE_USER'],
        url: "/first-stage",
        templateUrl: 'app/games/view/games.firststage.html',
        controller: 'betsGamesCtrl'
    }).state('bets.secondstage', {
        permissions: ['ROLE_USER'],
        url: "/second-stage",
        templateUrl: 'app/games/view/games.secondstage.html',
        controller: 'betsGamesCtrl'
    }).state('bets.groups', {
        //parent: 'site',
        permissions: ['ROLE_USER'],
        url: '/groups',
        views: {
            'content@': {
                templateUrl: 'app/games/view/games.groups.html',
                controller: 'betsGroupsCtrl'
            }
        }
    }).state('bets.manage', {
        //parent: 'site',
        permissions: ['ROLE_USER'],
        url: "/manage",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.view.html',
                controller: 'betsCtrl'
            }
        }
    });

    // Accounts
    $stateProvider.state('accounts', {
        parent: 'site',
        permissions: ['ROLE_ADMIN'],
        url: '/accounts',
        views: {
            'content@': {
                templateUrl: 'app/accounts/accounts.view.html',
                controller: 'accountCtrl'
            }
        }
    });

}).filter('capitalize', function () {
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
}).run(function ($rootScope, $state, $location, $anchorScroll, Auth) {

    Auth.init();

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if (!Auth.checkPermissionForView(toState)) {
            event.preventDefault();
            $state.go('join');
        }
    });

    $rootScope.scrollTo = function (id) {
        $location.hash(id);
        $anchorScroll.yOffset = 60;
        $anchorScroll();
    };
});



