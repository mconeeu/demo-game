package eu.mcone.demogame.util;

import eu.mcone.demogame.DemoGame;
import eu.mcone.demogame.listener.GeneralPlayerListener;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import org.bukkit.Bukkit;

public class DemoEndgameState extends EndGameState {

    @Override
    public void onStart(GameStateStartEvent event) {
        super.onStart(event);

        Bukkit.getOnlinePlayers().forEach(x -> {
            DemoGame.getInstance().getMessenger().sendTransl(x, "game.gamestate.end.winnerteam", GeneralPlayerListener.winnerTeam.getLabel());
        });

    }


}
