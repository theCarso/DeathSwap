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
        DeathSwap.getInstance().activeGame = this;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aGame successfully started!"));
        startTask();
    }

    public void startTask() {
        secondsLeft = secondsPerSwap;

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                doTask();
            }
        };

        runnable.runTaskTimer(DeathSwap.getInstance(), 20L, 20L);
    }

    public void doTask() {
        if (DeathSwap.getInstance().activeGame != this) {
            endGameWithWinner(null);
            return;
        }
        secondsLeft--;

        if (secondsLeft <= 0) {
            if (!player1.isOnline()) {
                endGameWithWinner(player2);
                return;
            }
            if (!player2.isOnline()) {
                endGameWithWinner(player1);
                return;
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lTeleporting..."));
            Location loc1 = player1.getLocation();
            Location loc2 = player2.getLocation();

            player1.teleport(loc2);
            player2.teleport(loc1);
            secondsLeft = secondsPerSwap;
        } else if (secondsLeft <= 15) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lSwapping in " + secondsLeft + " seconds."));
        }
    }

    public void endGameWithWinner(Player player) {
        runnable.cancel();
        DeathSwap.getInstance().activeGame = null;
        if (player == null) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c* Current game cancelled!"));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l>> " + player.getName() + " HAS WON!"));
        }
    }
}
