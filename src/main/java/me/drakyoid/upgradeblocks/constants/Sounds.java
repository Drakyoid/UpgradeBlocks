package me.drakyoid.upgradeblocks.constants;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum Sounds {

    SUCCESS(Sound.ANVIL_LAND),
    FAIL(Sound.NOTE_BASS);

    private Sound sound;

    Sounds(Sound sound) {
        this.sound = sound;
    }

    public void playSound(Player player, int pitchMultiplier) {
        Location playerLocation = player.getLocation();
        if (pitchMultiplier == 0) pitchMultiplier = 1;
        player.playSound(playerLocation, sound, 0.05F, (float) pitchMultiplier * 0.25F);
    }

}
