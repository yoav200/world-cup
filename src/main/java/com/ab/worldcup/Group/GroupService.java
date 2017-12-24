package com.ab.worldcup.Group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.MatchResultRepository;
import com.ab.worldcup.team.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupMatchRepository groupMatchRepository;

    @Autowired
    MatchResultRepository matchResultRepository;

    public boolean isGroupFinished(Group groupId){
        boolean groupMatchesFinished = true;
        List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
        for (GroupMatch groupMatch : groupMatches) {
            MatchResult matchResult = matchResultRepository.findOne(groupMatch.getMatchId());
            if(matchResult == null){
                groupMatchesFinished = false;
                break;
            }
        }
        return groupMatchesFinished;
    }

}
