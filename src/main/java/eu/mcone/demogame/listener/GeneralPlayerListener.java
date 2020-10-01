package eu.mcone.demogame.listener;

import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.util.DemoIngameState;
import eu.mcone.demogame.util.TeamManager;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.team.Team;
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

    private static boolean gameEnded = false;

    @EventHandler
    public void on(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerRespawnEvent event) {
        if (!gameEnded) {
            event.setRespawnLocation(DemoIngameState.getRandomSpawn());
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof Player || (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player))) {
            Player victimPlayer = (Player) event.getEntity();
            victimPlayer.setHealth(0);
        }
    }

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
    }
}
