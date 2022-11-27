package com.ab.worldcup.group;


import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:tiebreaking.properties")
public class TieBreakConfig {

    @Autowired
    Environment environment;

    @Autowired
    TeamRepository teamRepository;

    private static Map<Long, Integer> teamIdToRankMap;

    @PostConstruct
    public void init() {
        teamIdToRankMap = new HashMap<>();
        Group[] values = Group.values();
        for (Group group : values) {
            String currGroupConfig = this.environment.getProperty("Group" + group.name());
            if (StringUtils.isNoneBlank(currGroupConfig)) {
                String[] split = currGroupConfig.split(",");
                int rank = 4;
                for (String s : split) {
                    Team team = teamRepository.findByName(s);
                    teamIdToRankMap.put(team.getId(), rank);
                    rank--;
                }

            }
        }
    }

    public static Integer getTeamRank(Long teamId) {
        return teamIdToRankMap.getOrDefault(teamId, 0);
    }
}
