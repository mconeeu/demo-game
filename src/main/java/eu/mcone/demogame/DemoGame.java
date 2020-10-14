package eu.mcone.demogame;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.demogame.commands.PointsCommand;
import eu.mcone.demogame.listener.GeneralPlayerListener;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class DemoGame extends GamePlugin {

    /**
     * Contains the instance of the current running Minigame
     */
    @Getter
    private static DemoGame instance;
    /**
     * Contains all the spawnLocations
     */
    @Getter
    private final Map<Location, Long> spawnLocations = new HashMap<>();
    /**
     * Contains the {@link CoreWorld} on which the Minigame is played
     */
    @Getter
    private CoreWorld minigameWorld;

    public static int POINTS_TO_WIN = 10;


    /**
     * Basic Constructer which supers the Constructer of {@link GamePlugin}, sets the Plugin Name, Color, Key to get the translated prefix out of the Database & (sets Options)
     */
    public DemoGame() {
        super("demogame", ChatColor.RED, "demogame.prefix", Option.HOTBAR_SET_ITEMS, Option.BACKPACK_MANAGER_REGISTER_ALL_DEFAULT_CATEGORIES);
    }


    /**
     * Overrides the onGameEnable Class from {@link GamePlugin} which gets called on the initialisation of the Plugin
     * Sets the static {@link DemoGame} instance to this instance;
     * Gets a MinigameWorld by the Name "DemoGame" from the {@link eu.mcone.coresystem.api.bukkit.world.WorldManager} located in the {@link CoreSystem}
     * Takes a List of Objects type: {@link eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand}
     * Given: {@link PointsCommand}
     * Takes a List of Objects type: {@link org.bukkit.event.Listener}
     * Given: {@link GeneralPlayerListener}
     * Gets all Locations saved in the {@link CoreWorld} and puts them into the HashMap spawnLocations
     * Initializes the {@link eu.mcone.gameapi.api.player.PlayerManager}
     * Initializes the {@link eu.mcone.gameapi.api.damage.DamageLogger} (Important for the {@link GeneralPlayerListener} Class to get the Victims Damager)
     * Initializes the {@link eu.mcone.gameapi.api.backpack.BackpackManager}
     * Initializes the {@link TeamManager} and loads a set of Default {@link Team}s
     * Initializes the {@link eu.mcone.gameapi.api.gamestate.GameStateManager} and adds all the {@link eu.mcone.gameapi.api.gamestate.GameState} in the correct Order to a Pool
     * Sends a ConsoleMessage as confirmation of a successful start
     */
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

        getBackpackManager();

        getTeamManager().loadDefaultTeams();

        getGameStateManager().addGameState(new LobbyGameState()).addGameState(new DemoIngameState()).addGameState(new EndGameState()).startGame();

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");

    }

    /**
     * This Method is called as the Plugin stops it's service
     */
    @Override
    public void onGameDisable() {
        sendConsoleMessage("§cDemoGame disabled!");
    }

}
