package me.thecarso.deathswap.events;

import me.thecarso.deathswap.DeathSwap;
import me.thecarso.deathswap.games.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // when a player dies, check if they are part of the active game
        // if the player is in the active game, then the other player wins

        Game activeGame = DeathSwap.getInstance().activeGame;
        if (activeGame != null) {
            if (activeGame.getPlayer1() == event.getEntity()) {
                activeGame.endGameWithWinner(activeGame.getPlayer2());
                return;
            }
            if (activeGame.getPlayer2() == event.getEntity()) {
                activeGame.endGameWithWinner(activeGame.getPlayer1());
                return;
            }
        }
    }
}
