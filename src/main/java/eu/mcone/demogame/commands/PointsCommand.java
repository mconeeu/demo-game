package eu.mcone.demogame.commands;

import eu.mcone.coresystem.api.bukkit.broadcast.Messenger;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.util.TeamManager;
import org.bukkit.entity.Player;

public class PointsCommand extends CorePlayerCommand {

    public PointsCommand() {
        super("points", "group.team");
    }

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
