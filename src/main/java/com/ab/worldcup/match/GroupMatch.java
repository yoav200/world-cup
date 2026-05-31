package com.ab.worldcup.match;

import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Table(name = "group_match")
public class GroupMatch extends Match {

  @ManyToOne
  @JoinColumn(name = "homeTeam", referencedColumnName = "id")
  private Team homeTeam;

  @ManyToOne
  @JoinColumn(name = "awayTeam", referencedColumnName = "id")
  private Team awayTeam;

  @Enumerated(EnumType.STRING)
  private Group groupId;

  @Override
  public Stage getStageId() {
    return Stage.GROUP;
  }

  @Override
  public String toString() {
    return "Group " + groupId.name() + ",  "
        + homeTeam.getName()
        + " : "
        + awayTeam.getName()
        + (getResult() != null ? " (F)" : "");
    // + (getResult() != null ? " &#10004;" : "");
  }

  // used for the UI
  public boolean isReady() {
    return true;
  }

}
