package me.drakyoid.upgradeblocks;

import me.drakyoid.upgradeblocks.listener.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UpgradeBlocks extends JavaPlugin {

    @Override
    public void onEnable() {
        loadConfig();
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerInteract(this), this);
    }
}
