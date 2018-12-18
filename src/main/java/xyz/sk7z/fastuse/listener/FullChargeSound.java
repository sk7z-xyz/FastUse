package xyz.sk7z.fastuse.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.player_values.PlayerShotValues;

class FullChargeSound extends BukkitRunnable {
    private FastUse plugin;
    private Player player;

    public FullChargeSound(Player player, FastUse plugin) {
        this.player = player;
        this.plugin = plugin;

    }

    @Override
    public void run() {
        PlayerShotValues playerShotValues = plugin.getPlayerValues(player).getArrowShotValues();
        if (player.getWorld().getTime() - playerShotValues.getStart_tick() >= 20 || (player.getWorld().getTime() - playerShotValues.getStart_tick() >= 5 && playerShotValues.getElapsedTimeMillis() >= 1000)) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10, 10);
        } else {
            new FullChargeSound(player, plugin).runTaskLater(plugin, 1);
        }


    }
}
