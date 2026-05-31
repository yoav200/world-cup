package com.ab.worldcup.team;

import com.ab.worldcup.match.KnockoutMatchCode;
import com.ab.worldcup.match.Stage;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

public enum KnockoutTeamCode {
    // Group winners → Round of 32
    WINNER_GROUP_A(Stage.ROUND_OF_32),
    WINNER_GROUP_B(Stage.ROUND_OF_32),
    WINNER_GROUP_C(Stage.ROUND_OF_32),
    WINNER_GROUP_D(Stage.ROUND_OF_32),
    WINNER_GROUP_E(Stage.ROUND_OF_32),
    WINNER_GROUP_F(Stage.ROUND_OF_32),
    WINNER_GROUP_G(Stage.ROUND_OF_32),
    WINNER_GROUP_H(Stage.ROUND_OF_32),
    WINNER_GROUP_I(Stage.ROUND_OF_32),
    WINNER_GROUP_J(Stage.ROUND_OF_32),
    WINNER_GROUP_K(Stage.ROUND_OF_32),
    WINNER_GROUP_L(Stage.ROUND_OF_32),
    // Group runners-up → Round of 32
    RUNNER_UP_GROUP_A(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_B(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_C(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_D(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_E(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_F(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_G(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_H(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_I(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_J(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_K(Stage.ROUND_OF_32),
    RUNNER_UP_GROUP_L(Stage.ROUND_OF_32),
    // Best third-place teams → Round of 32
    THIRD_PLACE_ABCDF(Stage.ROUND_OF_32),
    THIRD_PLACE_CDFGH(Stage.ROUND_OF_32),
    THIRD_PLACE_CEFHI(Stage.ROUND_OF_32),
    THIRD_PLACE_EHIJK(Stage.ROUND_OF_32),
    THIRD_PLACE_BEFIJ(Stage.ROUND_OF_32),
    THIRD_PLACE_AEHIJ(Stage.ROUND_OF_32),
    THIRD_PLACE_EFGIJ(Stage.ROUND_OF_32),
    THIRD_PLACE_DEIJL(Stage.ROUND_OF_32),
    // Round of 32 winners → Round of 16
    WINNER_RT1(Stage.ROUND_OF_16),
    WINNER_RT2(Stage.ROUND_OF_16),
    WINNER_RT3(Stage.ROUND_OF_16),
    WINNER_RT4(Stage.ROUND_OF_16),
    WINNER_RT5(Stage.ROUND_OF_16),
    WINNER_RT6(Stage.ROUND_OF_16),
    WINNER_RT7(Stage.ROUND_OF_16),
    WINNER_RT8(Stage.ROUND_OF_16),
    WINNER_RT9(Stage.ROUND_OF_16),
    WINNER_RT10(Stage.ROUND_OF_16),
    WINNER_RT11(Stage.ROUND_OF_16),
    WINNER_RT12(Stage.ROUND_OF_16),
    WINNER_RT13(Stage.ROUND_OF_16),
    WINNER_RT14(Stage.ROUND_OF_16),
    WINNER_RT15(Stage.ROUND_OF_16),
    WINNER_RT16(Stage.ROUND_OF_16),
    // Round of 16 winners → Quarter-final
    WINNER_ROS1(Stage.QUARTER_FINAL),
    WINNER_ROS2(Stage.QUARTER_FINAL),
    WINNER_ROS3(Stage.QUARTER_FINAL),
    WINNER_ROS4(Stage.QUARTER_FINAL),
    WINNER_ROS5(Stage.QUARTER_FINAL),
    WINNER_ROS6(Stage.QUARTER_FINAL),
    WINNER_ROS7(Stage.QUARTER_FINAL),
    WINNER_ROS8(Stage.QUARTER_FINAL),
    // Quarter-final winners → Semi-final
    WINNER_QF1(Stage.SEMI_FINAL),
    WINNER_QF2(Stage.SEMI_FINAL),
    WINNER_QF3(Stage.SEMI_FINAL),
    WINNER_QF4(Stage.SEMI_FINAL),
    // Semi-final results → Final / Third place
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
            case WINNER_GROUP_I:
            case WINNER_GROUP_J:
            case WINNER_GROUP_K:
            case WINNER_GROUP_L:
            case RUNNER_UP_GROUP_A:
            case RUNNER_UP_GROUP_B:
            case RUNNER_UP_GROUP_C:
            case RUNNER_UP_GROUP_D:
            case RUNNER_UP_GROUP_E:
            case RUNNER_UP_GROUP_F:
            case RUNNER_UP_GROUP_G:
            case RUNNER_UP_GROUP_H:
            case RUNNER_UP_GROUP_I:
            case RUNNER_UP_GROUP_J:
            case RUNNER_UP_GROUP_K:
            case RUNNER_UP_GROUP_L:
            case THIRD_PLACE_ABCDF:
            case THIRD_PLACE_CDFGH:
            case THIRD_PLACE_CEFHI:
            case THIRD_PLACE_EHIJK:
            case THIRD_PLACE_BEFIJ:
            case THIRD_PLACE_AEHIJ:
            case THIRD_PLACE_EFGIJ:
            case THIRD_PLACE_DEIJL:
                return KnockoutTeamCodeType.GROUP_QUALIFIER;
            default:
                return KnockoutTeamCodeType.KNOCKOUT_MATCH_QUALIFIER;
        }
    }

    public Optional<KnockoutMatchCode> getKnockoutMatchCode() {
        switch (this) {
            case WINNER_RT1:
                return Optional.of(KnockoutMatchCode.RT1);
            case WINNER_RT2:
                return Optional.of(KnockoutMatchCode.RT2);
            case WINNER_RT3:
                return Optional.of(KnockoutMatchCode.RT3);
            case WINNER_RT4:
                return Optional.of(KnockoutMatchCode.RT4);
            case WINNER_RT5:
                return Optional.of(KnockoutMatchCode.RT5);
            case WINNER_RT6:
                return Optional.of(KnockoutMatchCode.RT6);
            case WINNER_RT7:
                return Optional.of(KnockoutMatchCode.RT7);
            case WINNER_RT8:
                return Optional.of(KnockoutMatchCode.RT8);
            case WINNER_RT9:
                return Optional.of(KnockoutMatchCode.RT9);
            case WINNER_RT10:
                return Optional.of(KnockoutMatchCode.RT10);
            case WINNER_RT11:
                return Optional.of(KnockoutMatchCode.RT11);
            case WINNER_RT12:
                return Optional.of(KnockoutMatchCode.RT12);
            case WINNER_RT13:
                return Optional.of(KnockoutMatchCode.RT13);
            case WINNER_RT14:
                return Optional.of(KnockoutMatchCode.RT14);
            case WINNER_RT15:
                return Optional.of(KnockoutMatchCode.RT15);
            case WINNER_RT16:
                return Optional.of(KnockoutMatchCode.RT16);
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

    public Optional<Pair<KnockoutTeamCode, KnockoutTeamCode>> getPrevStageTeams() {
        switch (this) {
            // Round of 32 winners → from group qualifiers
            case WINNER_RT1:
                return Optional.of(Pair.of(RUNNER_UP_GROUP_A, RUNNER_UP_GROUP_B));
            case WINNER_RT2:
                return Optional.of(Pair.of(WINNER_GROUP_E, THIRD_PLACE_ABCDF));
            case WINNER_RT3:
                return Optional.of(Pair.of(WINNER_GROUP_F, RUNNER_UP_GROUP_C));
            case WINNER_RT4:
                return Optional.of(Pair.of(WINNER_GROUP_C, RUNNER_UP_GROUP_F));
            case WINNER_RT5:
                return Optional.of(Pair.of(WINNER_GROUP_I, THIRD_PLACE_CDFGH));
            case WINNER_RT6:
                return Optional.of(Pair.of(RUNNER_UP_GROUP_E, RUNNER_UP_GROUP_I));
            case WINNER_RT7:
                return Optional.of(Pair.of(WINNER_GROUP_A, THIRD_PLACE_CEFHI));
            case WINNER_RT8:
                return Optional.of(Pair.of(WINNER_GROUP_L, THIRD_PLACE_EHIJK));
            case WINNER_RT9:
                return Optional.of(Pair.of(WINNER_GROUP_D, THIRD_PLACE_BEFIJ));
            case WINNER_RT10:
                return Optional.of(Pair.of(WINNER_GROUP_G, THIRD_PLACE_AEHIJ));
            case WINNER_RT11:
                return Optional.of(Pair.of(RUNNER_UP_GROUP_K, RUNNER_UP_GROUP_L));
            case WINNER_RT12:
                return Optional.of(Pair.of(WINNER_GROUP_H, RUNNER_UP_GROUP_J));
            case WINNER_RT13:
                return Optional.of(Pair.of(WINNER_GROUP_B, THIRD_PLACE_EFGIJ));
            case WINNER_RT14:
                return Optional.of(Pair.of(WINNER_GROUP_J, RUNNER_UP_GROUP_H));
            case WINNER_RT15:
                return Optional.of(Pair.of(WINNER_GROUP_K, THIRD_PLACE_DEIJL));
            case WINNER_RT16:
                return Optional.of(Pair.of(RUNNER_UP_GROUP_D, RUNNER_UP_GROUP_G));
            // Round of 16 winners → from R32 winners
            case WINNER_ROS1:
                return Optional.of(Pair.of(WINNER_RT2, WINNER_RT5));
            case WINNER_ROS2:
                return Optional.of(Pair.of(WINNER_RT1, WINNER_RT3));
            case WINNER_ROS3:
                return Optional.of(Pair.of(WINNER_RT4, WINNER_RT6));
            case WINNER_ROS4:
                return Optional.of(Pair.of(WINNER_RT7, WINNER_RT8));
            case WINNER_ROS5:
                return Optional.of(Pair.of(WINNER_RT11, WINNER_RT12));
            case WINNER_ROS6:
                return Optional.of(Pair.of(WINNER_RT9, WINNER_RT10));
            case WINNER_ROS7:
                return Optional.of(Pair.of(WINNER_RT14, WINNER_RT16));
            case WINNER_ROS8:
                return Optional.of(Pair.of(WINNER_RT13, WINNER_RT15));
            // Quarter-final winners → from R16 winners
            case WINNER_QF1:
                return Optional.of(Pair.of(WINNER_ROS1, WINNER_ROS2));
            case WINNER_QF2:
                return Optional.of(Pair.of(WINNER_ROS5, WINNER_ROS6));
            case WINNER_QF3:
                return Optional.of(Pair.of(WINNER_ROS3, WINNER_ROS4));
            case WINNER_QF4:
                return Optional.of(Pair.of(WINNER_ROS7, WINNER_ROS8));
            // Semi-final / Final
            case WINNER_SF1:
            case LOSER_SF1:
                return Optional.of(Pair.of(WINNER_QF1, WINNER_QF2));
            case WINNER_SF2:
            case LOSER_SF2:
                return Optional.of(Pair.of(WINNER_QF3, WINNER_QF4));
            case WINNER_THIRD_PLACE:
                return Optional.of(Pair.of(LOSER_SF1, LOSER_SF2));
            case WINNER_FINAL:
                return Optional.of(Pair.of(WINNER_SF1, WINNER_SF2));
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
            case WINNER_GROUP_I:
            case RUNNER_UP_GROUP_I:
                return Optional.of(Group.I);
            case WINNER_GROUP_J:
            case RUNNER_UP_GROUP_J:
                return Optional.of(Group.J);
            case WINNER_GROUP_K:
            case RUNNER_UP_GROUP_K:
                return Optional.of(Group.K);
            case WINNER_GROUP_L:
            case RUNNER_UP_GROUP_L:
                return Optional.of(Group.L);
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
            case WINNER_GROUP_I:
            case WINNER_GROUP_J:
            case WINNER_GROUP_K:
            case WINNER_GROUP_L:
                return true;
            default:
                return false;
        }
    }
}
