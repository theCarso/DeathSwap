package me.thecarso.deathswap;

import lombok.Getter;
import me.thecarso.deathswap.cmds.DSCommand;
import me.thecarso.deathswap.events.PlayerDeath;
import me.thecarso.deathswap.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathSwap extends JavaPlugin {

    public Game activeGame;

    private static @Getter
    DeathSwap instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("ds").setExecutor(new DSCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
