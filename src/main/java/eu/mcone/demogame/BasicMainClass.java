package eu.mcone.demogame;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class BasicMainClass extends GamePlugin {

    /**
     * Contains the instance of the current running Minigame
     */
    @Getter
    private static BasicMainClass instance;
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

    /**
     * Basic Constructer which supers the Constructer of {@link GamePlugin}, sets the Plugin Name, Color, Key to get the translated prefix out of the Database & (sets Options)
     */
    public BasicMainClass() {
        super("demogame", ChatColor.RED, "demogame.prefix", Option.HOTBAR_SET_ITEMS, Option.TEAM_MANAGER_DISABLE_WIN_METHOD);
    }


    /**
     * This Method is called as the Plugin gets initialised
     */
    @Override
    public void onGameEnable() {
        /*
         * Sets the static instance to this instance;
         */
        instance = this;
        /*
         * Gets a MinigameWorld by the Name "DemoGame" from the WorldManager located in the CoreSystem
         */
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("DemoGame");

        /*
         * Takes a List of Objects type: {@link eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand}
         */
        registerCommands(
        );

        /*
         * Takes a List of Objects type: {@link org.bukkit.event.Listener}
         */
        registerEvents(
        );

        /*
         * Gets all Locations saved in the {@link CoreWorld} and puts them into the HashMap spawnLocations
         */
        for (Map.Entry<String, CoreLocation> location : minigameWorld.getLocations().entrySet()) {
            if (location.getKey().startsWith("DemoGame-")) {
                spawnLocations.put(location.getValue().bukkit(), (System.currentTimeMillis() / 1000) - 5);
            }
        }

        /*
         * Initializes the {@link eu.mcone.gameapi.api.player.PlayerManager}
         */
        getPlayerManager();
        /*
         * Initializes the {@link eu.mcone.gameapi.api.damage.DamageLogger} (Important for the {@link GeneralPlayerListener} Class to get the Victims Damager)
         */
        getDamageLogger();
        /*
         * Initializes the {@link TeamManager} and loads a set of Default Teams
         */
        getTeamManager().loadDefaultTeams();
        /*
         * Initializes the {@link eu.mcone.gameapi.api.gamestate.GameStateManager} and adds all the {@link eu.mcone.gameapi.api.gamestate.GameState} in the correct Order to a Pool
         */
        getGameStateManager().addGameState(new LobbyGameState()).addGameState(new DemoIngameState()).addGameState(new EndGameState()).startGame();

        /*
         * Sends a ConsoleMessage
         */
        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");

    }

    /**
     * This Method is called as the Plugin stops it's service
     */
    @Override
    public void onGameDisable() {
        /*
         * Sends a ConsoleMessage
         */
        sendConsoleMessage("§cDemoGame disabled!");
    }

}

