package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.demogame.DemoGame;
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
        entry.setScore(2, "Team-Ziel: §b" + +DemoGame.POINTS_TO_WIN);
        entry.setScore(3, "");
        int counter = 4;
        for (Team team : DemoGame.getInstance().getTeamManager().getTeams()) {
            entry.setScore(counter, "00 " + team.getLabel());
            counter++;
        }
        entry.setScore(counter, "");
    }

    /**
     * Updates a {@link org.bukkit.scoreboard.Scoreboard}. ({@link org.bukkit.scoreboard.Scoreboard} Lines are set from the bottom up. (0 is bottom))
     * Gets the amount of points a {@link Team} has. (Gets returned by the Goals of the Team)
     *
     * @param cp
     * @param entry
     */
    @Override
    protected void onInGameReload(CorePlayer cp, CoreSidebarObjectiveEntry entry) {
        entry.setScore(2, "Team-Ziel: §b" + DemoGame.POINTS_TO_WIN);
        entry.setScore(3, "");
        int counter = 4;
        for (Team team : DemoGame.getInstance().getTeamManager().getTeams()) {
            int points = team.getGoals();
            if (points < 10) {
                entry.setScore(counter, "0" + points + " " + team.getLabel());
            } else {
                entry.setScore(counter, points + " " + team.getLabel());
            }
            counter++;
        }
        entry.setScore(counter, "");
    }
}
