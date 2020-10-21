package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.demogame.DemoGame;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DemoIngameState extends InGameState {

    public static boolean hasStarted = false;
    @Getter
    private static final Map<Player, Location> lastSpawnLocations = new HashMap<>();

    /**
     * Static ArrayList which contains the SpawnLocations
     */
    private static final List<Location> locations = new ArrayList<>();

    /**
     * Static Constructer which overrides the given Scoreboard with the selfmade one
     */
    static {
        setObjective(DemoScoreboard.class);
    }

    /**
     * Constructer which supers the Standard Constructer of the Mother Class and sets the Timeout to 30 Minutes
     */
    public DemoIngameState() {
        super(1800);
    }

    /**
     * Returns a random SpawnLocation and removes it from the List Locations if shouldRemove is true
     *
     * @return
     */
    public static Location getRandomSpawn(boolean shouldRemove) {
        Location location = locations.get(new Random().nextInt(locations.size() - 1));
        DemoGame.getInstance().getSpawnLocations().put(location, System.currentTimeMillis() / 1000);

        if (shouldRemove) {
            locations.remove(location);
        }

        return location;
    }

    /**
     * onStart will be started when the GameState {@link DemoIngameState} is reached
     *
     * Supers the onStart Method from the {@link InGameState} Class
     * Sets the Locationslist
     * Teleports every Spectator to the Spectator Location
     * Teleports every {@link org.bukkit.entity.Player} which is {@link GamePlayerState} PLAYING to a random Spawnlocation
     * Sets the {@link org.bukkit.scoreboard.Scoreboard}
     * Sends a Title
     * Sets the Items
     * Makes a Countdown
     * Sends translated Gamestart Message
     * Resets Locationslist
     *
     * @param event
     */
    @Override
    public void onStart(GameStateStartEvent event) {
        super.onStart(event);

        setLocations();
        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.SPECTATING).forEach(x -> {
            x.teleport(DemoGame.getInstance().getMinigameWorld().getLocation("game.spectator"));
        });
        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
            x.getInventory().clear();
            Location location = getRandomSpawn(true);
            x.teleport(location);
            x.setGameMode(GameMode.ADVENTURE);
            DemoIngameState.getLastSpawnLocations().put(x, location);
            handleCountdown(x, "§c5");
            for (Items item : Items.values()) {
                if (item.isEnchant()) {
                    ItemStack itemStack = new ItemBuilder(item.getMaterial()).displayName(CoreSystem.getInstance().getTranslationManager().get(item.getTranslation(), CoreSystem.getInstance().getCorePlayer(x))).unbreakable(true).enchantment(Enchantment.ARROW_INFINITE, 1).itemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).create();
                    x.getInventory().setItem(item.getSlot(), itemStack);
                } else {
                    x.getInventory().setItem(item.getSlot(), new ItemBuilder(item.getMaterial()).displayName(CoreSystem.getInstance().getTranslationManager().get(item.getTranslation(), CoreSystem.getInstance().getCorePlayer(x))).unbreakable(true).itemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).create());
                }
            }
        });

        Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
            DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                handleCountdown(x, "§64");
            });
            Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                    handleCountdown(x, "§e3");
                });
                Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                    DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                        handleCountdown(x, "§a2");
                    });
                    Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                            handleCountdown(x, "§21");
                        });
                        Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                            DemoGame.getInstance().getMessenger().broadcast(
                                    new SimpleBroadcast("demo.messages.gamestart"));
                            hasStarted = true;
                            setLocations();
                        }, 20);
                    }, 20);
                }, 20);
            }, 20);
        }, 20);
    }

    /**
     * Gets the Locations out of the Spawnlocations HashMap in the {@link DemoGame} Class and adds them to the Locationslist
     */
    public void setLocations() {
        for (Map.Entry<Location, Long> location : DemoGame.getInstance().getSpawnLocations().entrySet()) {
            if (((System.currentTimeMillis() / 1000) - location.getValue()) > 3) {
                locations.add(location.getKey());
            }
        }
    }

    /**
     * Creates a Title for a given Player and plays a Sound
     *
     * @param x
     * @param title
     */
    public void handleCountdown(Player x, String title) {
        CoreSystem.getInstance().createTitle().title(title).send(x);
        x.playSound(x.getLocation(), Sound.NOTE_BASS, 1, 1);
    }
}
