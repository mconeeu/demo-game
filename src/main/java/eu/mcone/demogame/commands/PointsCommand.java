package eu.mcone.demogame.commands;

import eu.mcone.coresystem.api.bukkit.broadcast.Messenger;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.util.TeamManager;
import org.bukkit.entity.Player;

public class PointsCommand extends CorePlayerCommand {

    /**
     * Constructer which supers the {@link CorePlayerCommand} Constructer. Sets the Commandname to "points" and adds the required Permission "group.team"
     */
    public PointsCommand() {
        super("points", "group.team");
    }

    /**
     * Overrides the onPlayerCommand Class from {@link CorePlayerCommand}
     * Saves the {@link Messenger} in a Variable
     * Sends a placeholder
     * Gets the Points of every {@link eu.mcone.gameapi.api.team.Team} and sends them with the Label of the {@link eu.mcone.gameapi.api.team.Team}
     * Sends a placeholder
     *
     * @param player
     * @param strings
     * @return
     */
    @Override
    public boolean onPlayerCommand(Player player, String[] strings) {
        Messenger m = DemoGame.getInstance().getMessenger();

        m.send(player, "==============Points==============");
        DemoGame.getInstance().getTeamManager().getTeams().forEach(x -> {
            m.send(player, x.getLabel() + " " + TeamManager.getPoints().get(x));
        });
        m.send(player, "==============Points==============");
        return false;
    }
}
