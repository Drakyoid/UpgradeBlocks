package me.drakyoid.upgradeblocks.utils;

import me.drakyoid.upgradeblocks.UpgradeBlocks;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Tiers {

    private List<Material> tiers;

    public Tiers(UpgradeBlocks plugin) {
        FileConfiguration config = plugin.getConfig();

        tiers = new ArrayList<>();
        setTiers(tiers, config);
    }

    private void setTiers(List<Material> tiers, FileConfiguration config) {
        for(String tierMaterial : config.getStringList("Tiers")) {
            tiers.add(Material.valueOf(tierMaterial));
        }
    }

    public void addTier(Material tierMaterial) {
        tiers.add(tierMaterial);
    }

    public void removeTier(Material tierMaterial) {
        tiers.remove(tierMaterial);
    }

    public List<Material> getTiers() {
        return tiers;
    }

    public Material getTier(int tierLevel) {
        if (tierLevel < 0 || tierLevel > tiers.size() - 1) return null;
        return tiers.get(tierLevel);
    }

    public int getTierLevel(Material tierMaterial) {
        // Returns -1 if not found
        return tiers.indexOf(tierMaterial);
    }



}
