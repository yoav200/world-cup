package com.ab.worldcup.events;

import com.ab.worldcup.match.Stage;

public record MatchResultEnteredEvent(Long matchId, Stage stage) {}
