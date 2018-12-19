package xyz.sk7z.fastuse;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sk7z.fastuse.player_values.PlayerShotValues;

public class FullChargeSound extends BukkitRunnable {
    private FastUse plugin;
    private Player player;
    private PlayerShotValues playerShotValues;

    public FullChargeSound(Player player, FastUse plugin, PlayerShotValues playerShotValues) {
        this.player = player;
        this.plugin = plugin;
        this.playerShotValues = playerShotValues;

    }

    @Override
    public void run() {
        if (player.getWorld().getTime() - playerShotValues.getStart_tick() >= 20 || (player.getWorld().getTime() - playerShotValues.getStart_tick() >= 5 && playerShotValues.getElapsedTimeMillis() >= 1000)) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10, 10);
        } else {
            new FullChargeSound(player, plugin,playerShotValues).runTaskLater(plugin, 1);
        }


    }
}
