package com.ab.worldcup.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    GroupMatchRepository groupMatchRepository;


    public List<GroupMatch> getAllGroupMatches(){
        return groupMatchRepository.findAll();
    }

    @PostConstruct
    private void init(){
        getAllGroupMatches().stream().forEach(t-> t.toString());
    }
}
