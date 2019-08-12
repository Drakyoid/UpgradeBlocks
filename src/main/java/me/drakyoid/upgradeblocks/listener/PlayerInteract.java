package me.drakyoid.upgradeblocks.listener;

import me.drakyoid.upgradeblocks.UpgradeBlocks;
import me.drakyoid.upgradeblocks.constants.Messages;
import me.drakyoid.upgradeblocks.constants.Sounds;
import me.drakyoid.upgradeblocks.utils.Tiers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteract implements Listener {

    private Tiers tiers;
    private Material toolMaterial;

    public PlayerInteract(UpgradeBlocks plugin) {
        tiers = new Tiers(plugin);
        toolMaterial = Material.valueOf(plugin.getConfig().getString("Tool"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        //Ignore non right clicks and upgrade tool
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() != null && event.getItem().getType() != null &&
                event.getItem().getType() == toolMaterial) return;

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractWithTool(PlayerInteractEvent event) {
        //Ignore non right clicks and make sure they have the upgrade tool
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack tool = event.getItem();
        if (tool == null || tool.getType() == null) return;
        if (event.getItem().getType() != null && tool.getType() != toolMaterial) return;

        event.setCancelled(true);

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        //Get the tier level of the block they right clicked
        Material blockMaterial = clickedBlock.getType();
        int blockTierLevel = tiers.getTierLevel(blockMaterial);
        if (blockTierLevel == -1) return;

        Material nextMaterial = tiers.getTier(blockTierLevel + 1);
        if (nextMaterial == null) return;

        Player player = event.getPlayer();

        //Get the blocks surrounding the block
        List<Block> surroundingBlocks = getSurroundingBlocks(clickedBlock);
        int blockAmount = surroundingBlocks.size() - 1;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            //Check if they have the right material
            if (itemStack.getType() == nextMaterial) {
                if (itemStack.getAmount() >= blockAmount) {
                    //Remove from their inventory
                    player.getInventory().removeItem(new ItemStack(nextMaterial, blockAmount));
                    for (Block relativeBlock : surroundingBlocks) {
                        //Replace them with the upgraded material
                        relativeBlock.setType(nextMaterial);
                    }
                    //Reduce durability of upgrade tool
                    tool.setDurability((short) (tool.getDurability() + 1));
                    tool.setItemMeta(tool.getItemMeta());
                    if (tool.getDurability() >= tool.getType().getMaxDurability()) {
                        player.getInventory().remove(tool);
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
                    }

                    Sounds.SUCCESS.playSound(player, blockTierLevel + 1);
                    return;
                } else {
                    //They don't have enough materials
                    break;
                }
            }
        }

        //Don't allow upgrade
        Messages.NOT_ENOUGH.send(player);
        Sounds.FAIL.playSound(player, blockTierLevel);

    }

    private List<Block> getSurroundingBlocks(Block block) {
        int radius = 1;
        Location location = block.getLocation();

        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block relativeBlock = location.getWorld().getBlockAt(x, y, z);
                    if (relativeBlock.getType() == block.getType()) {
                        blocks.add(relativeBlock);
                    }
                }
            }
        }
        blocks.add(block); //Also add the initial block
        return blocks;
    }

}
