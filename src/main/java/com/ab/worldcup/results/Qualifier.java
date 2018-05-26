package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "qualifier")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Qualifier {

    @Enumerated(EnumType.STRING)
    @Id
    private KnockoutTeamCode knockoutTeamCode;

    @ManyToOne
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Stage stageId;


    public Stage getStageId() {
        if (stageId == null) {
            stageId = knockoutTeamCode.getStageId();
        }
        return stageId;
    }
}
