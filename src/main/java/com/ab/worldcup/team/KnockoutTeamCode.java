package com.ab.worldcup.team;

import com.ab.worldcup.match.KnockoutMatchCode;

import java.util.Optional;

public enum KnockoutTeamCode {
    WINNER_GROUP_A,
    WINNER_GROUP_B,
    WINNER_GROUP_C,
    WINNER_GROUP_D,
    WINNER_GROUP_E,
    WINNER_GROUP_F,
    WINNER_GROUP_G,
    WINNER_GROUP_H,
    RUNNER_UP_GROUP_A,
    RUNNER_UP_GROUP_B,
    RUNNER_UP_GROUP_C,
    RUNNER_UP_GROUP_D,
    RUNNER_UP_GROUP_E,
    RUNNER_UP_GROUP_F,
    RUNNER_UP_GROUP_G,
    RUNNER_UP_GROUP_H,
    WINNER_ROS1,
    WINNER_ROS2,
    WINNER_ROS3,
    WINNER_ROS4,
    WINNER_ROS5,
    WINNER_ROS6,
    WINNER_ROS7,
    WINNER_ROS8,
    WINNER_QF1,
    WINNER_QF2,
    WINNER_QF3,
    WINNER_QF4,
    WINNER_SF1,
    WINNER_SF2,
    LOSER_SF1,
    LOSER_SF2,
    WINNER_THIRD_PLACE,
    WINNER_FINAL;

    public KnockoutTeamCodeType getType() {
        switch (this) {
            case WINNER_GROUP_A:
            case WINNER_GROUP_B:
            case WINNER_GROUP_C:
            case WINNER_GROUP_D:
            case WINNER_GROUP_E:
            case WINNER_GROUP_F:
            case WINNER_GROUP_G:
            case WINNER_GROUP_H:
            case RUNNER_UP_GROUP_A:
            case RUNNER_UP_GROUP_B:
            case RUNNER_UP_GROUP_C:
            case RUNNER_UP_GROUP_D:
            case RUNNER_UP_GROUP_E:
            case RUNNER_UP_GROUP_F:
            case RUNNER_UP_GROUP_G:
            case RUNNER_UP_GROUP_H:
                return KnockoutTeamCodeType.GROUP_QUALIFIER;
            default:
                return KnockoutTeamCodeType.KNOCKOUT_MATCH_QULIFIER;
        }
    }

    public Optional<KnockoutMatchCode> getKnockoutMatchCode() {
        switch (this) {
            case WINNER_ROS1:
                return Optional.of(KnockoutMatchCode.ROS1);
            case WINNER_ROS2:
                return Optional.of(KnockoutMatchCode.ROS2);
            case WINNER_ROS3:
                return Optional.of(KnockoutMatchCode.ROS3);
            case WINNER_ROS4:
                return Optional.of(KnockoutMatchCode.ROS4);
            case WINNER_ROS5:
                return Optional.of(KnockoutMatchCode.ROS5);
            case WINNER_ROS6:
                return Optional.of(KnockoutMatchCode.ROS6);
            case WINNER_ROS7:
                return Optional.of(KnockoutMatchCode.ROS7);
            case WINNER_ROS8:
                return Optional.of(KnockoutMatchCode.ROS8);
            case WINNER_QF1:
                return Optional.of(KnockoutMatchCode.QF1);
            case WINNER_QF2:
                return Optional.of(KnockoutMatchCode.QF2);
            case WINNER_QF3:
                return Optional.of(KnockoutMatchCode.QF3);
            case WINNER_QF4:
                return Optional.of(KnockoutMatchCode.QF4);
            case WINNER_SF1:
                return Optional.of(KnockoutMatchCode.SF1);
            case WINNER_SF2:
                return Optional.of(KnockoutMatchCode.SF2);
            case WINNER_THIRD_PLACE:
                return Optional.of(KnockoutMatchCode.TP);
            case WINNER_FINAL:
                return Optional.of(KnockoutMatchCode.F);
            default:
                return Optional.empty();
        }
    }

    public Optional<Group> getRelevantGroup() {
        switch (this) {
            case WINNER_GROUP_A:
            case RUNNER_UP_GROUP_A:
                return Optional.of(Group.A);
            case WINNER_GROUP_B:
            case RUNNER_UP_GROUP_B:
                return Optional.of(Group.B);
            case WINNER_GROUP_C:
            case RUNNER_UP_GROUP_C:
                return Optional.of(Group.C);
            case WINNER_GROUP_D:
            case RUNNER_UP_GROUP_D:
                return Optional.of(Group.D);
            case WINNER_GROUP_E:
            case RUNNER_UP_GROUP_E:
                return Optional.of(Group.E);
            case WINNER_GROUP_F:
            case RUNNER_UP_GROUP_F:
                return Optional.of(Group.F);
            case WINNER_GROUP_G:
            case RUNNER_UP_GROUP_G:
                return Optional.of(Group.G);
            case WINNER_GROUP_H:
            case RUNNER_UP_GROUP_H:
                return Optional.of(Group.H);
            default:
                return Optional.empty();
        }
    }

    public boolean isGroupWinner() {
        switch (this) {
            case WINNER_GROUP_A:
            case WINNER_GROUP_B:
            case WINNER_GROUP_C:
            case WINNER_GROUP_D:
            case WINNER_GROUP_E:
            case WINNER_GROUP_F:
            case WINNER_GROUP_G:
            case WINNER_GROUP_H:
                return true;
            default:
                return false;
        }
    }
}
