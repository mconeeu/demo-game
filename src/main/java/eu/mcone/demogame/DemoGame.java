package eu.mcone.demogame;

import eu.mcone.gameapi.api.GamePlugin;
import org.bukkit.ChatColor;

public class DemoGame extends GamePlugin {

    public DemoGame() {
        super("DemoGame", ChatColor.RED, "demogame.prefix");
    }

    @Override
    public void onGameEnable() {
        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onGameDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

}
