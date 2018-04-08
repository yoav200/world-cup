'use strict';

angular.module('worldcup').directive('bracketsTeam', function () {
    return {
        scope: {
            match: '=data'
        },
        templateUrl: 'app/directives/view/brackets.team.template.html',
        link: function (scope, element, attributes) {
            scope.homeTeamClass = '';
            scope.awayTeamClass = '';

            var winnerCss = 'winner';
            var loserCss = 'loser';

            if(scope.match.stageId == 'THIRD_PLACE') {
                winnerCss += 'winner third';
                loserCss += 'loser fourth';
            } else if(scope.match.stageId == 'FINAL') {
                winnerCss += 'winner first';
                loserCss += 'loser second';
            }

            if(scope.match.result) {
                if(scope.match.result.winner == 'HOME_TEAM_WON') {
                    scope.homeTeamClass = winnerCss;
                    scope.awayTeamClass = loserCss;
                } else {
                    scope.homeTeamClass = loserCss;
                    scope.awayTeamClass = winnerCss;
                }
            }
        }
    }
});

