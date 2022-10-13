'use strict';

/**
 * Main AngularJS Web Application
 */
var app = angular.module('worldcup', [
    'ui.router',
    'ui.router.stateHelper',
    'ui.bootstrap',
    'ngCookies',
    'xeditable',
    'ngResource',
    'ngAnimate',
    'ngSanitize',
    'angular-loading-bar',
    'angular-growl']);


/**
 * Configure the Routes using ui router
 */
app.config(function ($urlRouterProvider, $httpProvider, growlProvider) {
    // $locationProvider.html5Mode(true);
    // For any unmatched url, redirect to /
    $urlRouterProvider.otherwise("/");

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    $httpProvider.defaults.headers.common["Accept"] = "application/json";
    $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
    $httpProvider.interceptors.push(growlProvider.serverMessagesInterceptor);

    growlProvider.globalDisableCountDown(true);
    growlProvider.globalPosition('top-center');
    growlProvider.globalTimeToLive({success: 10000, error: 10000, warning: 10000, info: 10000});

    // alternatively, register the interceptor via an anonymous factory
    $httpProvider.interceptors.push(function($q, $timeout, $window) {
        return {
            'responseError': function(errorResponse) {
                switch (errorResponse.status) {
                    case 403:
                        $timeout(function () {
                            $window.location = '/';
                        }, 500);
                        break;
                }
                return $q.reject(errorResponse);
            }
        };
    });

}).config(function ($stateProvider) {
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
    }).state('rules', {
        parent: 'site',
        url: '/rules',
        views: {
            'content@': {
                templateUrl: 'app/rules/rules.view.html'
            }
        }
    }).state('join', {
        parent: 'site',
        url: "/join",
        views: {
            'content@': {
                templateUrl: 'app/register/register.view.html',
                controller: 'registerCtrl'
            }
        }
    }).state('login', {
        parent: 'site',
        url: "/login",
        views: {
            'content@': {
                templateUrl: 'app/login/login.view.html',
                controller: 'loginCtrl'
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
        url: "/manage?matchId",
        views: {
            'content@': {
                templateUrl: 'app/admin/admin.view.html',
                controller: 'adminCtrl'
            }
        }
    });

    // Bets
    $stateProvider.state('bets', {
        abstract: true,
        parent: 'site'
    }).state('bets.overview', {
        permissions: ['ROLE_USER'],
        url: "/bets/overview",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.overview.html',
                controller: 'betsOverviewCtrl'
            }
        }
     }).state('bets.groups', {
        permissions: ['ROLE_USER'],
        url: "/bets/groups",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.groups.view.html',
                controller: 'betsGroupsCtrl'
            }
        }
    }).state('bets.matches', {
        permissions: ['ROLE_USER'],
        url: "/bets/matches?matchId",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.matches.html',
                controller: 'betsMatchesCtrl'
            }
        }
    }).state('bets.qualifier', {
        permissions: ['ROLE_USER'],
        url: "/bets/qualifier",
        views: {
            'content@': {
                templateUrl: 'app/bets/view/bets.qualifier.html',
                controller: 'betsQualifierCtrl'
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
}).run(function ($rootScope, $state, Auth) {

    Auth.init();

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if (!Auth.checkPermissionForView(toState)) {
            Auth.setStateUrl(toState.name);
            event.preventDefault();
            $state.go('login');
        }
    });
}).run(function($rootScope, $location, $anchorScroll, editableOptions) {

    $rootScope.scrollTo = function (id) {
        $location.hash(id);
        $anchorScroll.yOffset = 60;
        $anchorScroll();
    };

    // inline edit style
    // bootstrap3 theme. Can be also 'bs2', 'default'
    editableOptions.theme = 'bs3';
});



