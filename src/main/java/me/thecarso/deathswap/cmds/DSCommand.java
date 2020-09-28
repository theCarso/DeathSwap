package me.thecarso.deathswap.cmds;

import me.thecarso.deathswap.DeathSwap;
import me.thecarso.deathswap.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DSCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("deathswap.admin")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission!"));
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("end")) {
                if (DeathSwap.getInstance().activeGame != null) {
                    DeathSwap.getInstance().activeGame.endGameWithWinner(null);
                }
            }
        }
        if (args.length == 2) {
            Player player1 = Bukkit.getPlayer(args[0]);
            Player player2 = Bukkit.getPlayer(args[1]);
            if (player1 != null && player2 != null) {
                new Game(player1, player2);
                sender.sendMessage("Started game with " + player1.getName() + " and " + player2.getName());
                return true;
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments. /ds end OR /ds <player> <player>"));
        return true;
    }
}
