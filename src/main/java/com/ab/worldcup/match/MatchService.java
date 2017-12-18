package com.ab.worldcup.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    GroupMatchRepository groupMatchRepository;

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;

    private final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public List<Match> getAllMatches(){
        List<Match> allMatches = groupMatchRepository.findAll().stream().collect(Collectors.toList());
        allMatches.addAll(knockoutMatchRepository.findAll());
        return allMatches;
    }

    @PostConstruct
    private void init(){
        getAllMatches().stream().forEach(t-> logger.info(t.toString()) );
    }
}
