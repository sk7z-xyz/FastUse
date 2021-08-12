package xyz.sk7z.fastuse.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.FastUse;

import java.util.Arrays;
import java.util.List;

/**
 * fastuse ServerStatusコマンドクラス
 *
 * @author sk7z
 */
public class ServerStatusCommand extends CommandFrame {

    FastUse plg;

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public ServerStatusCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;
        setAuthBlock(true);
        setAuthConsole(true);

    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "fastuse.serverStatus";
    }

    /**
     * 処理実行部
     *
     * @param sender コマンド送信者
     * @param args   パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {

        if (!checkRange(sender, args, 0, 0)) return true;

        int viewDistance = plg.getServer().getViewDistance();

        long start_time = System.nanoTime();

        Utl.sendPluginMessage(plg, sender, "----------Worlds----------");
        plg.getServer().getWorlds().forEach(world -> {
            String format = "%-13s: Players:%2d Entities:%5d Chunks:%5d";
            Utl.sendPluginMessage(plg, sender, String.format(format, world.getName(), world.getPlayers().size(), world.getLivingEntities().size(), world.getLoadedChunks().length));
        });
        Utl.sendPluginMessage(plg, sender, "----------Players----------");

        playerSearch:
        {
            for (Player player : plg.getServer().getOnlinePlayers()) {
                int entity_count = 0;
                World world = player.getWorld();
                Location loc = player.getLocation();

                List<Chunk> loadingChunkList = Arrays.asList(world.getLoadedChunks());
                for (int i = -viewDistance; i <= viewDistance; i++) {
                    for (int j = -viewDistance; j <= viewDistance; j++) {
                        Location target_loc = loc.clone();
                        target_loc.add(i * 16, 0, j * 16);
                        Chunk chunk = world.getChunkAt(target_loc);
                        if (loadingChunkList.contains(chunk)) {
                            entity_count += Arrays.stream(chunk.getEntities()).filter(entity -> entity instanceof LivingEntity).count();
                        }
                        //1000ミリ秒経過した場合はキャンセルする
                        if ((System.nanoTime() - start_time) / 1000 / 1000 >= 1000) {
                            Utl.sendPluginMessage(plg, sender, "TimeOutしました");
                            break playerSearch;
                        }
                    }
                }
                String format = "%-15s: [%-13s:%4d,%4d] Entities:%d";
                Utl.sendPluginMessage(plg, sender, String.format(format, player.getName(), player.getWorld().getName(), loc.getBlockX(), loc.getBlockZ(), entity_count));
            }
        }

        long end_time = System.nanoTime();
        Utl.sendPluginMessage(plg, sender, "測定時間" + (end_time - start_time) / 1000 / 1000 + "ms");
        return true;
    }
}
