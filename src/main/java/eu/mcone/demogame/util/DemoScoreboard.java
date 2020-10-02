package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.scoreboard.InGameObjective;
import eu.mcone.gameapi.api.team.Team;

/**
 * {@link DemoScoreboard} contains the Methods which are used to create a {@link org.bukkit.scoreboard.Scoreboard}
 */
public class DemoScoreboard extends InGameObjective {

    /**
     * Registers a {@link org.bukkit.scoreboard.Scoreboard}. ({@link org.bukkit.scoreboard.Scoreboard} Lines are set from the bottom up. (0 is bottom))
     * @param cp
     * @param entry
     */
    @Override
    protected void onInGameRegister(CorePlayer cp, CoreSidebarObjectiveEntry entry) {
        entry.setScore(0, "Team-Ziel: §b40");
        entry.setScore(1, "");
        Team team = GameAPI.getInstance().getGamePlayer(cp).getTeam();
        entry.setScore(2, "00 " + team.getLabel());
        entry.setScore(3, "");
    }

    /**
     * Updates a {@link org.bukkit.scoreboard.Scoreboard}. ({@link org.bukkit.scoreboard.Scoreboard} Lines are set from the bottom up. (0 is bottom))
     * Gets the amount of points a {@link Team} has out of the HashMap created in {@link TeamManager}
     * @param cp
     * @param entry
     */
    @Override
    protected void onInGameReload(CorePlayer cp, CoreSidebarObjectiveEntry entry) {
        entry.setScore(0, "Team-Ziel: §b40");
        entry.setScore(1, "");
        Team team = GameAPI.getInstance().getGamePlayer(cp).getTeam();
        int points = TeamManager.getPoints().get(team);
        if (points < 10) {
            entry.setScore(2, "0" + points + " " + team.getLabel());
        } else {
            entry.setScore(2, points + " " + team.getLabel());
        }
        entry.setScore(3, "");
    }
}
