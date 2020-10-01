package eu.mcone.demogame.util;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;

import java.util.HashMap;

public class TeamManager {

    @Getter
    private static final HashMap<Team, Integer> points = new HashMap<>();


    public static boolean checkResult(Team team) {
        return TeamManager.getPoints().get(team) >= 40;
    }

}
