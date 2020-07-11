package xyz.sk7z.fastuse;

import me.confuser.barapi.BarAPI;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class TpsBar extends BukkitRunnable {

    String format = "TPS:%.2f(%.2f%%) Ping:%dms Time:%02d:%02d";
    Instant lastSendInstant = Instant.now();
    Plugin plugin;

    TpsBar(FastUse plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (ChronoUnit.SECONDS.between(lastSendInstant, Instant.now()) > 10) {
            lastSendInstant = Instant.now();

            float tps_bar = Math.min(new Double(Lag.getTPS(200) * 5).floatValue(),100f);

            plugin.getServer().getOnlinePlayers().forEach(player -> {
                ItemMeta offHandItemMeta = player.getInventory().getItemInOffHand().getItemMeta();
                if (offHandItemMeta != null && offHandItemMeta.getDisplayName().equals("[tps]")) {
                    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                    long time = player.getWorld().getTime() + 6000;
                    if (time >= 24000) {
                        time -= 24000;
                    }
                    long hour = time / 1000;
                    long min = (long) (time % 1000f / 1000 * 60);

                    String msg = String.format(format, Lag.getTPS(200),Lag.getTPS(200)*5, entityPlayer.ping, hour, min);
                    BarAPI.setMessage(player, msg);
                    BarAPI.setHealth(player, tps_bar);
                }else{
                    BarAPI.removeBar(player);
                }
            });
        }


    }
}
