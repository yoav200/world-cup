package com.ab.worldcup.team;

import com.ab.worldcup.match.KnockoutMatchCode;
import com.ab.worldcup.match.Stage;

import java.util.Optional;

public enum KnockoutTeamCode {
    WINNER_GROUP_A(Stage.ROUND_OF_16),
    WINNER_GROUP_B(Stage.ROUND_OF_16),
    WINNER_GROUP_C(Stage.ROUND_OF_16),
    WINNER_GROUP_D(Stage.ROUND_OF_16),
    WINNER_GROUP_E(Stage.ROUND_OF_16),
    WINNER_GROUP_F(Stage.ROUND_OF_16),
    WINNER_GROUP_G(Stage.ROUND_OF_16),
    WINNER_GROUP_H(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_A(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_B(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_C(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_D(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_E(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_F(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_G(Stage.ROUND_OF_16),
    RUNNER_UP_GROUP_H(Stage.ROUND_OF_16),
    WINNER_ROS1(Stage.QUARTER_FINAL),
    WINNER_ROS2(Stage.QUARTER_FINAL),
    WINNER_ROS3(Stage.QUARTER_FINAL),
    WINNER_ROS4(Stage.QUARTER_FINAL),
    WINNER_ROS5(Stage.QUARTER_FINAL),
    WINNER_ROS6(Stage.QUARTER_FINAL),
    WINNER_ROS7(Stage.QUARTER_FINAL),
    WINNER_ROS8(Stage.QUARTER_FINAL),
    WINNER_QF1(Stage.SEMI_FINAL),
    WINNER_QF2(Stage.SEMI_FINAL),
    WINNER_QF3(Stage.SEMI_FINAL),
    WINNER_QF4(Stage.SEMI_FINAL),
    WINNER_SF1(Stage.FINAL),
    WINNER_SF2(Stage.FINAL),
    LOSER_SF1(Stage.THIRD_PLACE),
    LOSER_SF2(Stage.THIRD_PLACE),
    WINNER_THIRD_PLACE(Stage.THIRD_PLACE_WINNER),
    WINNER_FINAL(Stage.WINNER);

    private final Stage stageId;

    KnockoutTeamCode(Stage stageId) {
        this.stageId = stageId;
    }

    public Stage getStageId() {
        return stageId;
    }

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
                return KnockoutTeamCodeType.KNOCKOUT_MATCH_QUALIFIER;
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
            case LOSER_SF1:
                return Optional.of(KnockoutMatchCode.SF1);
            case WINNER_SF2:
            case LOSER_SF2:
                return Optional.of(KnockoutMatchCode.SF2);
            case WINNER_THIRD_PLACE:
                return Optional.of(KnockoutMatchCode.TP);
            case WINNER_FINAL:
                return Optional.of(KnockoutMatchCode.F);
            default:
                return Optional.empty();
        }
    }

//    public Optional<Pair<KnockoutTeamCode, KnockoutTeamCode>> getPrevStageTeams() {
//        switch (this) {
//            case WINNER_ROS1:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_C, RUNNER_UP_GROUP_D));
//            case WINNER_ROS2:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_A, RUNNER_UP_GROUP_B));
//            case WINNER_ROS3:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_B, RUNNER_UP_GROUP_A));
//            case WINNER_ROS4:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_D, RUNNER_UP_GROUP_C));
//            case WINNER_ROS5:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_E, RUNNER_UP_GROUP_F));
//            case WINNER_ROS6:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_G, RUNNER_UP_GROUP_H));
//            case WINNER_ROS7:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_F, RUNNER_UP_GROUP_E));
//            case WINNER_ROS8:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_GROUP_H, RUNNER_UP_GROUP_G));
//            case WINNER_QF1:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_ROS1, WINNER_ROS2));
//            case WINNER_QF2:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_ROS5, WINNER_ROS6));
//            case WINNER_QF3:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_ROS3, WINNER_ROS4));
//            case WINNER_QF4:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_ROS7, WINNER_ROS8));
//            case WINNER_SF1:
//            case LOSER_SF1:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_QF1, WINNER_QF2));
//            case WINNER_SF2:
//            case LOSER_SF2:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_QF3, WINNER_QF4));
//            case WINNER_THIRD_PLACE:
//                return Optional.of(Pair.of(KnockoutTeamCode.LOSER_SF1, LOSER_SF2));
//            case WINNER_FINAL:
//                return Optional.of(Pair.of(KnockoutTeamCode.WINNER_SF1, WINNER_SF2));
//            default:
//                return Optional.empty();
//        }
//    }
//
//    public static Set<KnockoutTeamCode> getNextEffectedStage(Group group) {
//        if (group == null) {
//            throw new IllegalArgumentException("group cannot be null");
//        }
//        switch (group) {
//            case A:
//                return ImmutableSet.of(WINNER_GROUP_A, RUNNER_UP_GROUP_A);
//            case B:
//                return ImmutableSet.of(WINNER_GROUP_B, RUNNER_UP_GROUP_B);
//            case C:
//                return ImmutableSet.of(WINNER_GROUP_C, RUNNER_UP_GROUP_C);
//            case D:
//                return ImmutableSet.of(WINNER_GROUP_D, RUNNER_UP_GROUP_D);
//            case E:
//                return ImmutableSet.of(WINNER_GROUP_E, RUNNER_UP_GROUP_E);
//            case F:
//                return ImmutableSet.of(WINNER_GROUP_F, RUNNER_UP_GROUP_F);
//            case G:
//                return ImmutableSet.of(WINNER_GROUP_G, RUNNER_UP_GROUP_G);
//            case H:
//                return ImmutableSet.of(WINNER_GROUP_H, RUNNER_UP_GROUP_H);
//            default:
//                return new HashSet<>();
//        }
//    }
//
//    public static KnockoutTeamCode getNextEffectedStage(KnockoutTeamCode code) {
//        if (code == null) {
//            throw new IllegalArgumentException("code cannot be null");
//        }
//        // Round of 16
//        if (code == WINNER_GROUP_C || code == RUNNER_UP_GROUP_D) {
//            return WINNER_ROS1;
//        } else if (code == WINNER_GROUP_A || code == RUNNER_UP_GROUP_B) {
//            return WINNER_ROS2;
//        } else if (code == WINNER_GROUP_E || code == RUNNER_UP_GROUP_F) {
//            return WINNER_ROS5;
//        } else if (code == WINNER_GROUP_G || code == RUNNER_UP_GROUP_H) {
//            return WINNER_ROS6;
//        } else if (code == WINNER_GROUP_B || code == RUNNER_UP_GROUP_A) {
//            return WINNER_ROS3;
//        } else if (code == WINNER_GROUP_D || code == RUNNER_UP_GROUP_C) {
//            return WINNER_ROS4;
//        } else if (code == WINNER_GROUP_F || code == RUNNER_UP_GROUP_E) {
//            return WINNER_ROS7;
//        } else if (code == WINNER_GROUP_H || code == RUNNER_UP_GROUP_G) {
//            return WINNER_ROS8;
//
//            // Quarter finals
//        } else if (code == WINNER_ROS1 || code == WINNER_ROS2) {
//            return WINNER_QF1;
//        } else if (code == WINNER_ROS5 || code == WINNER_ROS6) {
//            return WINNER_QF2;
//        } else if (code == WINNER_ROS3 || code == WINNER_ROS4) {
//            return WINNER_QF3;
//        } else if (code == WINNER_ROS7 || code == WINNER_ROS8) {
//            return WINNER_QF4;
//
//            // semi finals
//        } else if (code == WINNER_QF1 || code == WINNER_QF2) {
//            return WINNER_SF1;
//        } else if (code == WINNER_QF3 || code == WINNER_QF4) {
//            return WINNER_SF2;
//        }
//
//        // third place
//        else if (code == LOSER_SF1 || code == LOSER_SF2) {
//            return WINNER_THIRD_PLACE;
//
//            // finals
//        } else if (code == WINNER_SF1 || code == WINNER_SF2) {
//            return WINNER_FINAL;
//        }
//
//        return null;
//    }


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
