package eu.mcone.demogame.util;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.demogame.DemoGame;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DemoIngameState extends InGameState {

    private static final List<Location> locations = new ArrayList<>();

    public static Location getRandomSpawn() {
        Location location = locations.get(new Random().nextInt(locations.size() - 1));
        DemoGame.getInstance().getSpawnLocations().put(location, System.currentTimeMillis() / 1000);

        locations.remove(location);

        return location;
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        super.onStart(event);

        setLocations();
        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.SPECTATING).forEach(x -> {
            x.teleport(DemoGame.getInstance().getMinigameWorld().getLocation("game.spectator"));
        });
        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
            x.teleport(getRandomSpawn());
            CoreSystem.getInstance().createTitle().title("§c5").send(x);
            for (Items item : Items.values()) {
                x.getInventory().setItem(item.getSlot(), new ItemBuilder(item.getMaterial()).displayName(CoreSystem.getInstance().getTranslationManager().get(item.getTranslation(), CoreSystem.getInstance().getCorePlayer(x))).unbreakable(true).itemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).create());
            }
        });
        Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
            DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                CoreSystem.getInstance().createTitle().title("§64").send(x);
            });
            Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                    CoreSystem.getInstance().createTitle().title("§e3").send(x);
                });
                Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                    DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                        CoreSystem.getInstance().createTitle().title("§a2").send(x);
                    });
                    Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                        DemoGame.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(x -> {
                            CoreSystem.getInstance().createTitle().title("§21").send(x);
                        });
                        Bukkit.getScheduler().runTaskLater(DemoGame.getInstance(), () -> {
                            DemoGame.getInstance().getMessenger().broadcast(
                                    new SimpleBroadcast("demo.messages.gamestart"));
                            setLocations();
                        }, 20);
                    }, 20);
                }, 20);
            }, 20);
        }, 20);
    }

    public void setLocations() {
        for (Map.Entry<Location, Long> location : DemoGame.getInstance().getSpawnLocations().entrySet()) {
            if (((System.currentTimeMillis() / 1000) - location.getValue()) > 3) {
                locations.add(location.getKey());
            }
        }
    }
}
