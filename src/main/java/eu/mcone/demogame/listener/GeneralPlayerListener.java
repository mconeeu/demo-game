package eu.mcone.demogame.listener;

import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.demogame.util.DemoScoreboard;
import eu.mcone.demogame.util.Items;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.broadcast.KillBroadcast;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.team.Team;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class GeneralPlayerListener implements Listener {

    /**
     * Checks if a Team would win the round
     *
     * @param team
     * @return
     */
    public static boolean checkResult(Team team) {
        return team.getGoals() >= DemoGame.POINTS_TO_WIN;
    }

    /**
     * Will be triggered when the {@link Player} achieves a {@link org.bukkit.Achievement}
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} {@link FoodLevelChangeEvent} changes
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to enter a {@link org.bukkit.material.Bed}
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to empty a bukket
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to drop a item
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to drop a item
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to manipulate a {@link org.bukkit.entity.ArmorStand}
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when the {@link Player} tries to interacts with something
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!DemoIngameState.hasStarted) {
            event.setCancelled(true);
        }
    }

    /**
     * Will be triggered when the {@link Player} tries to fish
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerFishEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    /**
     * Will be triggered when a {@link org.bukkit.entity.Entity} gets damaged
     * The {@link org.bukkit.event.Event} gets cancelled
     *
     * @param event
     */
    @EventHandler
    public void on(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause dc = event.getCause();
        if (dc == EntityDamageEvent.DamageCause.FALL
                || dc == EntityDamageEvent.DamageCause.DROWNING
                || dc == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                || dc == EntityDamageEvent.DamageCause.LAVA
                || dc == EntityDamageEvent.DamageCause.FIRE_TICK
                || dc == EntityDamageEvent.DamageCause.FIRE
        ) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }

    /**
     * Will be triggered when the {@link Player} tries to respawn
     * The Respawnlocation will be set to a random Spawnlocation if the game didn't end yet. (Static Boolean gameEnded = false)
     *
     * @param event
     */
    @EventHandler
    public void on(PlayerRespawnEvent event) {
        event.getPlayer().setVelocity(new Vector(0, 0, 0));
        if (DemoGame.getInstance().getGameStateManager().getRunning() instanceof DemoIngameState) {
            event.setRespawnLocation(DemoIngameState.getRandomSpawn(false));
        }
    }

    /**
     * Will be triggered when a {@link org.bukkit.entity.Entity} get damaged by an other {@link org.bukkit.entity.Entity}
     * Checks if the {@link org.bukkit.entity.Entity} is a {@link Player} and if the Damager is a {@link Player} or the Shooter (if the Damager is a Arrow) is a {@link Player}
     *
     * @param event
     */
    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (DemoGame.getInstance().getGameStateManager().getRunning() instanceof DemoIngameState) {
            Player victim = (Player) event.getEntity();
            if (event.getEntity() instanceof Player) {
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (damager.getItemInHand().getType().equals(Items.IRON_SWORD.getMaterial())) {
                        if (!DemoGame.getInstance().getGamePlayer(victim).getTeam().equals(DemoGame.getInstance().getGamePlayer(damager).getTeam())) {
                            handleDeath(victim, damager);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else if (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
                    if (!DemoGame.getInstance().getGamePlayer(victim).getTeam().equals(DemoGame.getInstance().getGamePlayer((Player) ((Arrow) event.getDamager()).getShooter()).getTeam())) {
                        handleDeath(victim, (Player) ((Arrow) event.getDamager()).getShooter());
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    /**
     * Will be triggered when the {@link Player} dies
     * Checks who was the Killer (Will be get from the {@link eu.mcone.gameapi.api.damage.DamageLogger})
     * Gets the {@link Team} of the Killer
     * Adds a point to the Killers {@link Team} located in the Teams Goals
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
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);
        Player killer = event.getEntity().getKiller() != null ? event.getEntity().getKiller() : DemoGame.getInstance().getDamageLogger().getKiller(event.getEntity());

        if (killer != null) {
            Team team = GameAPI.getInstance().getGamePlayer(killer).getTeam();
            GamePlayer gp = GameAPI.getInstance().getGamePlayer(killer);
            gp.addGoals(1);

            DemoGame.getInstance().getMessenger().broadcast(new KillBroadcast(killer, event.getEntity()));
            event.setKeepInventory(true);
            event.getEntity().spigot().respawn();
            DemoGame.getInstance().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING).forEach(x -> x.getCorePlayer().getScoreboard().reload());

            if (checkResult(team)) {
                DemoGame.getInstance().getTeamManager().stopGameWithWinner(team);
            }
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getFrom();
        if (DemoGame.getInstance().getGameStateManager().getRunning() instanceof DemoIngameState && !DemoIngameState.hasStarted) {
            player.teleport(location);
        }
    }

    /**
     * Handles the Death
     *
     * @param victim
     */
    public void handleDeath(Player victim, Player killer) {
        victim.setHealth(0.1);
        victim.playSound(victim.getLocation(), Sound.ANVIL_BREAK, 1, 1);
        killer.playSound(victim.getLocation(), Sound.LEVEL_UP, 1, 1);
    }
}
