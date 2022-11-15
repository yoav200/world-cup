'use strict';

angular.module('worldcup').directive('team', function () {
    return {
        restrict: 'E',
        scope: {
            team: '=data'
        },
        template: '<span ng-if="team">'
                    + '<img title="Rank: {{team.fifaRanking}}" class="flag" data-ng-src="/images/teams/{{team.confederation | spaceToUnderscore}}/{{team.code}}.png"/>&nbsp;&nbsp;'
                    + '{{team.name | capitalize }}'
                + '</span>',
    }
});

