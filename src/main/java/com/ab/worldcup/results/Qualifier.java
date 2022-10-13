package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
