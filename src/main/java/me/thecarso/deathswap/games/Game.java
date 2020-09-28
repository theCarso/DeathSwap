package me.thecarso.deathswap.games;

import lombok.Getter;
import me.thecarso.deathswap.DeathSwap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
    final int secondsPerSwap = 60 * 5;
    int secondsLeft;

    private @Getter
    Player player1, player2;

    private @Getter
    BukkitRunnable runnable;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        startGame();
    }

    public void startGame() {
        // sets this game as the active game- there can only be ONE active game at a time.
        DeathSwap.getInstance().activeGame = this;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aGame successfully started!"));
        startTask();
    }

    public void startTask() {
        // TODO make seconds per swap configurable
        secondsLeft = secondsPerSwap;

        // Run this task every second.
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                doTask();
            }
        };

        runnable.runTaskTimer(DeathSwap.getInstance(), 20L, 20L);
    }

    public void doTask() {
        // cancel the game if it is no longer active (another game is active
        if (DeathSwap.getInstance().activeGame != this) {
            endGameWithWinner(null);
            return;
        }
        // always subtract one second at the start of the method
        secondsLeft--;

        // if the time is up and a player is offline, the other player wins
        if (secondsLeft <= 0) {
            if (!player1.isOnline()) {
                endGameWithWinner(player2);
                return;
            }
            if (!player2.isOnline()) {
                endGameWithWinner(player1);
                return;
            }
            // if both players are online, teleport each player to the other player's location.
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lTeleporting..."));
            Location loc1 = player1.getLocation();
            Location loc2 = player2.getLocation();

            player1.teleport(loc2);
            player2.teleport(loc1);
            // reset swap time once players are teleported.
            secondsLeft = secondsPerSwap;
        } else if (secondsLeft <= 15) {
            // counts down when 15 seconds left until the swap.
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lSwapping in " + secondsLeft + " seconds."));
        }
    }

    // ends game by cancelling the loop, and removing active game
    public void endGameWithWinner(Player player) {
        runnable.cancel();
        DeathSwap.getInstance().activeGame = null;
        // if no winner (different game is active) then broadcast cancel
        // if there is a winner, then broadcast the winner in bold
        if (player == null) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c* Current game cancelled!"));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l>> " + player.getName() + " HAS WON!"));
        }
    }
}
