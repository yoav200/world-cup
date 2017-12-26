package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:points.properties")
public class PointsConfig {
    @Autowired
    Environment environment;

    private static Map<Stage,Integer> qualifierPoints;
    private static int correctWinnerPoints;
    private static int exactScorePoints;

    @PostConstruct
    public void init(){
        correctWinnerPoints = Integer.parseInt(this.environment.getProperty(PointsType.WINNER.name()));
        exactScorePoints = Integer.parseInt(this.environment.getProperty(PointsType.EXACT_SCORE.name()));
        qualifierPoints = new HashMap<>();
        Stage[] values = Stage.values();
        for (Stage stage : values) {
            if(Stage.GROUP.equals(stage)){
                continue;
            }
            Integer stageQualifierPoints = Integer.valueOf(this.environment.getProperty(PointsType.QUALIFIER.name() + "." + stage.name()));
            qualifierPoints.put(stage,stageQualifierPoints);
        }
    }

    public static Integer getCorrectWinnerPoints() {
        return correctWinnerPoints;
    }

    public static Integer getExactScorePoints() {
        return exactScorePoints;
    }

    public static Integer getQualifierPoints(Stage stage) {
        return qualifierPoints.get(stage);
    }

}
