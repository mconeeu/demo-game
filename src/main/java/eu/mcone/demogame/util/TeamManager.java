package eu.mcone.demogame.util;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;

import java.util.HashMap;

public class TeamManager {

    /**
     * Creates a HashMap which will save the points which belong to a {@link Team}
     */
    @Getter
    private final static HashMap<Team, Integer> points = new HashMap<>();


    /**
     * Checks if the given {@link Team} winns
     * @param team
     * @return
     */
    public static boolean checkResult(Team team) {
        return TeamManager.getPoints().get(team) >= 40;
    }

}
