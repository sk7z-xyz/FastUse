package xyz.sk7z.fastuse.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ServerStatusCommand {
    public static void showStatus(PluginFrame plg, Player player) {

        boolean[] time_out = {false};
        long start_time = System.nanoTime();
        Utl.sendPluginMessage(plg, player, "----------Worlds----------");
        plg.getServer().getWorlds().forEach(world -> {
            String format = "%-13s: Players:%2d Entities:%5d Chunks:%5d";
            Utl.sendPluginMessage(plg, player, String.format(format, world.getName(), world.getPlayers().size(), world.getLivingEntities().size(), world.getLoadedChunks().length));
        });
        Utl.sendPluginMessage(plg, player, "----------Players----------");


        plg.getServer().getOnlinePlayers().forEach(onlinePlayer -> {

            int viewDistance = 10;
            int entity_count = 0;

            Location loc = onlinePlayer.getLocation();
            World world = onlinePlayer.getWorld();
            String[] target_worlds = {"home01", "home02", "world", "world_the_end", "pvp01"};


            if (Arrays.asList(target_worlds).contains(world.getName())) {

                List<Chunk> loadingChunkList = Arrays.asList(world.getLoadedChunks());

                for (int i = -viewDistance; i <= viewDistance; i++) {
                    for (int j = -viewDistance; j <= viewDistance; j++) {
                        if (time_out[0]) {
                            break;
                        }
                        Location target_loc = loc.clone();
                        target_loc.add(i * 16, 0, j * 16);
                        Chunk chunk = world.getChunkAt(target_loc);
                        if (loadingChunkList.contains(chunk)) {
                            entity_count += Arrays.stream(chunk.getEntities()).filter(entity -> entity instanceof LivingEntity).count();
                        }
                        if ((System.nanoTime() - start_time) / 1000 / 1000 >= 1000) {
                            time_out[0] = true;
                        }
                    }
                }
                String format = "%-15s: [%-13s:%4d,%4d] Entities:%d";
                if (!time_out[0]) {
                    Utl.sendPluginMessage(plg, player, String.format(format, onlinePlayer.getName(), onlinePlayer.getWorld().getName(), loc.getBlockX(), loc.getBlockZ(), entity_count));
                }


            }


        });
        if (time_out[0]) {
            Utl.sendPluginMessage(plg, player, "TimeOutしました");
        }
        long end_time = System.nanoTime();
        Utl.sendPluginMessage(plg, player, "測定時間" + (end_time - start_time) / 1000 / 1000 + "ms");
    }

}