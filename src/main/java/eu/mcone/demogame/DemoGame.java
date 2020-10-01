package eu.mcone.demogame;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.demogame.commands.PointsCommand;
import eu.mcone.demogame.listener.GeneralPlayerListener;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.demogame.util.TeamManager;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class DemoGame extends GamePlugin {

    @Getter
    private static DemoGame instance;
    @Getter
    private final Map<Location, Long> spawnLocations = new HashMap<>();
    @Getter
    private CoreWorld minigameWorld;


    public DemoGame() {
        super("demogame", ChatColor.RED, "demogame.prefix", Option.HOTBAR_SET_ITEMS, Option.TEAM_MANAGER_DISABLE_WIN_METHOD);
    }


    @Override
    public void onGameEnable() {
        instance = this;
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("DemoGame");

        registerCommands(
                new PointsCommand()
        );

        registerEvents(
                new GeneralPlayerListener()
        );

        for (Map.Entry<String, CoreLocation> location : minigameWorld.getLocations().entrySet()) {
            if (location.getKey().startsWith("DemoGame-")) {
                spawnLocations.put(location.getValue().bukkit(), (System.currentTimeMillis() / 1000) - 5);
            }
        }

        getPlayerManager();
        getDamageLogger();
        getTeamManager().loadDefaultTeams();
        for (Team team : getTeamManager().getTeams()) {
            TeamManager.getPoints().put(team, 0);
        }
        getGameStateManager().addGameState(new LobbyGameState()).addGameState(new DemoIngameState()).addGameState(new EndGameState()).startGame();

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");

    }

    @Override
    public void onGameDisable() {
        sendConsoleMessage("§cDemoGame disabled!");
    }

}
