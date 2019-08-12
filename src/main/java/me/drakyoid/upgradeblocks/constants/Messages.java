package me.drakyoid.upgradeblocks.constants;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Messages {

    PREFIX(ChatColor.translateAlternateColorCodes('&', "&r[&4Server&r] ")),
    WRONG_TIER(Messages.PREFIX + ChatColor.translateAlternateColorCodes('&',
            "&cCan't upgrade this block, you can only upgrade one tier at a time!")),
    NOT_ENOUGH(Messages.PREFIX + ChatColor.translateAlternateColorCodes('&',
            "&cYou do not have enough materials, gather more or upgrade one at a time!"));

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }

    public void send(Player player) {
        player.sendMessage(message);
    }
}
