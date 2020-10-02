package eu.mcone.demogame.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.demogame.util.DemoScoreboard;
import eu.mcone.demogame.util.TeamManager;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class GeneralPlayerListener implements Listener {

    /**
     * Static Boolean which is by Standard false
     * but will be set to true when the result of the checkResult Method in {@link TeamManager} returns true
     */
    private static boolean gameEnded = false;

    /**
     * Will be triggered when the {@link Player} achieves a {@link org.bukkit.Achievement}
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} {@link FoodLevelChangeEvent} changes
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to enter a {@link org.bukkit.material.Bed}
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to empty a bukket
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to drop a item
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to manipulate a {@link org.bukkit.entity.ArmorStand}
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to interacts with something
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to fish
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(PlayerFishEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when a {@link org.bukkit.entity.Entity} gets damaged
     * The {@link org.bukkit.event.Event} gets cancelled
     * @param event
     */
    @EventHandler
    public void on(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to respawn
     * The Respawnlocation will be set to a random Spawnlocation if the game didn't end yet. (Static Boolean gameEnded = false)
     * @param event
     */
    @EventHandler
    public void on(PlayerRespawnEvent event) {
        if (!gameEnded) {
            event.setRespawnLocation(DemoIngameState.getRandomSpawn(false));
        }
    }

    /**
     * Will be triggered when a {@link org.bukkit.entity.Entity} get damaged by an other {@link org.bukkit.entity.Entity}
     * Checks if the {@link org.bukkit.entity.Entity} is a {@link Player} and if the Damager is a {@link Player} or the Shooter (if the Damager is a Arrow) is a {@link Player}
     * @param event
     */
    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof Player || (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player))) {
            Player victimPlayer = (Player) event.getEntity();
            victimPlayer.setHealth(0);
        }
    }

    /**
     * Will be triggered when the {@link Player} dies
     * Checks who was the Killer (Will be get from the {@link eu.mcone.gameapi.api.damage.DamageLogger})
     * Gets the {@link Team} of the Killer
     * Adds a point to the Killers {@link Team} in the HashMap located in {@link TeamManager}
     * Sends Kill-Messages which are saved in the MongoDB under the given Key
     * Sets KeepInventory to true
     * Respawns the Victim
     * Check if the Killers {@link Team} would win the game (If yes: Stops game with Winner-{@link Team} which is the {@link Team} of the Killer and sets gameEnded to true)
     * Goes trough every {@link Player} in the Killers {@link Team} and reloads the {@link DemoScoreboard}
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller() != null ? event.getEntity().getKiller() : DemoGame.getInstance().getDamageLogger().getKiller(event.getEntity());
        Team team = GameAPI.getInstance().getGamePlayer(killer).getTeam();
        TeamManager.getPoints().put(team, TeamManager.getPoints().get(team) + 1);

        DemoGame.getInstance().getMessenger().broadcast(new SimpleBroadcast("demo.messages.killer", killer));
        DemoGame.getInstance().getMessenger().broadcast(new SimpleBroadcast("demo.messages.victim", event.getEntity()));
        event.setKeepInventory(true);
        event.getEntity().spigot().respawn();
        if (TeamManager.checkResult(team)) {
            DemoGame.getInstance().getTeamManager().stopGameWithWinner(team);
            gameEnded = true;
        }
        team.getPlayers().forEach(x -> {
            x.getCorePlayer().getScoreboard().reload();
        });
    }
}
