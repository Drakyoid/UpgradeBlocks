package me.drakyoid.upgradeblocks.listener;

import me.drakyoid.upgradeblocks.UpgradeBlocks;
import me.drakyoid.upgradeblocks.constants.Messages;
import me.drakyoid.upgradeblocks.constants.Sounds;
import me.drakyoid.upgradeblocks.utils.Tiers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerInteract implements Listener {

    private Tiers tiers;

    public PlayerInteract(UpgradeBlocks plugin) {
        tiers = new Tiers(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //Ignore non right clicks and the off hand
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) return; //Ignore the offhand


        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        //Check if player is holding shift
        Player player = event.getPlayer();
        if (player.isSneaking()) return;

        //Get the tier level of the block they right clicked
        Material blockMaterial = clickedBlock.getType();
        int blockTierLevel = tiers.getTierLevel(blockMaterial);
        if (blockTierLevel == -1) return;

        ItemStack itemInHand = event.getItem();
        if (itemInHand == null) return;

        //Get the tier level of the block in their hand
        Material handMaterial = itemInHand.getType();
        int itemTierLevel = tiers.getTierLevel(handMaterial);
        if (itemTierLevel == -1) return;

        //Can't upgrade same tier
        if (itemTierLevel == blockTierLevel) return;

        //Can't upgrade if item is tier below
        if (itemTierLevel < blockTierLevel) return;

        event.setCancelled(true);

        //Item tier is greater than block tier
        if (itemTierLevel == blockTierLevel + 1) {
            //Allow upgrade
            itemInHand.setAmount(itemInHand.getAmount() - 1);
            clickedBlock.setType(tiers.getTier(itemTierLevel));
            Sounds.SUCCESS.playSound(player, itemTierLevel);
        } else {
            //Don't allow upgrade
            Messages.WRONG_TIER.send(player);
            Sounds.FAIL.playSound(player, itemTierLevel);
        }

    }

}
