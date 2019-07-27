# UpgradeBlocks
Spigot plugin to upgrade blocks using a tier list. It has a specific use case, but I figured I would release it to the public so everyone can use it.

## Setup
When first installing the plugin, it will create a config.yml file. In this file, you can set the tiers for upgrading.

For example, the default config is:

```
Tiers:
  - OAK_PLANKS
  - COBBLESTONE
  - IRON_BLOCK
  - DIAMOND_BLOCK
```
So, the tiers would go: OAK_PLANKS -> COBBLESTONE -> IRON_BLOCK -> DIAMOND_BLOCK

Meaning when you right click an oak plank with a cobblestone it will upgrade, then you can upgrade to iron blocks by right clicking the cobblestone with an iron block, and so on.

**When adding or changing tiers in the config.yml, you must use the [spigot material enumerators](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)!**

## Download
[SpigotMC](https://www.spigotmc.org/resources/upgradeblocks.69759/)
