'use strict';


angular.module('worldcup').controller('betsQualifierCtrl', function ($rootScope, $scope, Bets, Teams, growl) {

    $scope.qualifiers = {
        WINNER_GROUP_A: undefined,
        WINNER_GROUP_B: undefined,
        WINNER_GROUP_C: undefined,
        WINNER_GROUP_D: undefined,
        WINNER_GROUP_E: undefined,
        WINNER_GROUP_F: undefined,
        WINNER_GROUP_G: undefined,
        WINNER_GROUP_H: undefined,
        RUNNER_UP_GROUP_A: undefined,
        RUNNER_UP_GROUP_B: undefined,
        RUNNER_UP_GROUP_C: undefined,
        RUNNER_UP_GROUP_D: undefined,
        RUNNER_UP_GROUP_E: undefined,
        RUNNER_UP_GROUP_F: undefined,
        RUNNER_UP_GROUP_G: undefined,
        RUNNER_UP_GROUP_H: undefined,
        WINNER_ROS1: undefined,
        WINNER_ROS2: undefined,
        WINNER_ROS3: undefined,
        WINNER_ROS4: undefined,
        WINNER_ROS5: undefined,
        WINNER_ROS6: undefined,
        WINNER_ROS7: undefined,
        WINNER_ROS8: undefined,
        WINNER_QF1: undefined,
        WINNER_QF2: undefined,
        WINNER_QF3: undefined,
        WINNER_QF4: undefined,
        WINNER_SF1: undefined,
        WINNER_SF2: undefined,
        LOSER_SF1: undefined,
        LOSER_SF2: undefined,
        WINNER_THIRD_PLACE: undefined,
        WINNER_FINAL: undefined
    };

    $scope.teamsForSelect = {
        WINNER_GROUP_A: [],
        WINNER_GROUP_B: [],
        WINNER_GROUP_C: [],
        WINNER_GROUP_D: [],
        WINNER_GROUP_E: [],
        WINNER_GROUP_F: [],
        WINNER_GROUP_G: [],
        WINNER_GROUP_H: [],
        RUNNER_UP_GROUP_A: [],
        RUNNER_UP_GROUP_B: [],
        RUNNER_UP_GROUP_C: [],
        RUNNER_UP_GROUP_D: [],
        RUNNER_UP_GROUP_E: [],
        RUNNER_UP_GROUP_F: [],
        RUNNER_UP_GROUP_G: [],
        RUNNER_UP_GROUP_H: [],
        WINNER_ROS1: [],
        WINNER_ROS2: [],
        WINNER_ROS3: [],
        WINNER_ROS4: [],
        WINNER_ROS5: [],
        WINNER_ROS6: [],
        WINNER_ROS7: [],
        WINNER_ROS8: [],
        WINNER_QF1: [],
        WINNER_QF2: [],
        WINNER_QF3: [],
        WINNER_QF4: [],
        WINNER_SF1: [],
        WINNER_SF2: [],
        THIRD_PLACE: [],
        FINAL: []
    };


    var getQualifiers = function () {
        Bets.getQualifiers().then(function (response) {
            var qualifiersList = response.qualifiersList;
            angular.forEach(qualifiersList, function (qualifiers, index) {
                $scope.qualifiers[qualifiers.knockoutTeamCode] = qualifiers.team;
                $scope.selectionChanged(qualifiers.knockoutTeamCode);
            });
            //console.log("qualifiers:", $scope.qualifiers);
        });

    };

    $scope.saveQualifiers = function () {
        var qualifiersData = [];
        for (var key in $scope.qualifiers) {
            if ($scope.qualifiers.hasOwnProperty(key) && $scope.qualifiers[key]) {
                var qualifier = {
                    knockoutTeamCode: key,
                    team: $scope.qualifiers[key],
                    stageId: undefined
                };
                qualifiersData.push(qualifier);
            }
        }

        var qualifiersList = {
            qualifiersList: qualifiersData
        };

        Bets.setQualifiers(qualifiersList).then(function (response) {
            getQualifiers();
            growl.success('You\'re bet saved successfully.', {title: 'Success!'});
        });
    };


    $scope.teamsForStage = function (code) {
        return $scope.teamsForSelect[code];
    };

    function setFlagToSelect(code) {
        if ($scope.qualifiers[code]) {
            var team = $scope.qualifiers[code];
            var flag = "/images/teams/" + team.confederation + "/" + team.code + ".png";
            var css = 'transparent url(' + flag + ') no-repeat right top';
            angular.element(('#' + code)).css({'background': css});
        } else {
            angular.element(('#' + code)).css({'background': ''});
        }
    }

    function checkAndUpdateForSelect(codes, codeToUpdate) {
        setFlagToSelect(codes[0]);
        setFlagToSelect(codes[1]);
        if ($scope.qualifiers[codes[0]] && $scope.qualifiers[codes[1]]) {
            $scope.teamsForSelect[codeToUpdate] = [$scope.qualifiers[codes[0]], $scope.qualifiers[codes[1]]];
        }

        if (codes.includes('WINNER_SF2') || codes.includes('WINNER_SF1')) {
            var loser1, loser2;
            $scope.teamsForSelect.THIRD_PLACE = [];
            if ($scope.qualifiers.WINNER_SF1) {
                loser1 = $scope.teamsForSelect.WINNER_SF1.filter(function (team) {
                    return team.name !== $scope.qualifiers.WINNER_SF1.name;
                });
                $scope.qualifiers.LOSER_SF1 = loser1[0];
                $scope.teamsForSelect.THIRD_PLACE.push(loser1[0]);
            }
            if ($scope.qualifiers.WINNER_SF2) {
                loser2 = $scope.teamsForSelect.WINNER_SF2.filter(function (team) {
                    return team.name !== $scope.qualifiers.WINNER_SF2.name;
                });
                $scope.qualifiers.LOSER_SF2 = loser2[0];
                $scope.teamsForSelect.THIRD_PLACE.push(loser2[0]);
            }
        }
    }

    $scope.selectionChanged = function (code) {
        if (['WINNER_GROUP_C', 'RUNNER_UP_GROUP_D'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_C', 'RUNNER_UP_GROUP_D'], 'WINNER_ROS1');
        } else if (['WINNER_GROUP_A', 'RUNNER_UP_GROUP_B'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_A', 'RUNNER_UP_GROUP_B'], 'WINNER_ROS2');
        } else if (['WINNER_GROUP_E', 'RUNNER_UP_GROUP_F'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_E', 'RUNNER_UP_GROUP_F'], 'WINNER_ROS5');
        } else if (['WINNER_GROUP_G', 'RUNNER_UP_GROUP_H'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_G', 'RUNNER_UP_GROUP_H'], 'WINNER_ROS6');
        } else if (['WINNER_GROUP_B', 'RUNNER_UP_GROUP_A'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_B', 'RUNNER_UP_GROUP_A'], 'WINNER_ROS3');
        } else if (['WINNER_GROUP_D', 'RUNNER_UP_GROUP_C'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_D', 'RUNNER_UP_GROUP_C'], 'WINNER_ROS4');
        } else if (['WINNER_GROUP_F', 'RUNNER_UP_GROUP_E'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_F', 'RUNNER_UP_GROUP_E'], 'WINNER_ROS7');
        } else if (['WINNER_GROUP_H', 'RUNNER_UP_GROUP_G'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_GROUP_H', 'RUNNER_UP_GROUP_G'], 'WINNER_ROS8');
        } else if (['WINNER_ROS1', 'WINNER_ROS2'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_ROS1', 'WINNER_ROS2'], 'WINNER_QF1');
        } else if (['WINNER_ROS5', 'WINNER_ROS6'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_ROS5', 'WINNER_ROS6'], 'WINNER_QF2');
        } else if (['WINNER_ROS3', 'WINNER_ROS4'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_ROS3', 'WINNER_ROS4'], 'WINNER_QF3');
        } else if (['WINNER_ROS7', 'WINNER_ROS8'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_ROS7', 'WINNER_ROS8'], 'WINNER_QF4');
        } else if (['WINNER_QF1', 'WINNER_QF2'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_QF1', 'WINNER_QF2'], 'WINNER_SF1');
        } else if (['WINNER_QF3', 'WINNER_QF4'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_QF3', 'WINNER_QF4'], 'WINNER_SF2');
        } else if (['WINNER_SF1', 'WINNER_SF2'].includes(code)) {
            checkAndUpdateForSelect(['WINNER_SF1', 'WINNER_SF2'], 'FINAL');
        } else {
            console.log("no case for me", code)
        }
    };


    var getAllBets = function () {
        Bets.getAllBets().then(function (response) {
            //
        });
    };

    var getTeams = function () {
        Teams.getAllTeams().then(function (response) {
            angular.forEach(response, function (team, index) {
                var winner = 'WINNER_GROUP_' + team.groupId;
                var runnerUp = 'RUNNER_UP_GROUP_' + team.groupId;
                $scope.teamsForSelect[winner].push(team);
                $scope.teamsForSelect[runnerUp].push(team);
            });
        });
    };

    var init = function () {
        getAllBets();
        getTeams();
        getQualifiers()
    };

    init();

});