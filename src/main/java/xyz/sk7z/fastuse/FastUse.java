package xyz.sk7z.fastuse;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.command.FastUseCommand;
import xyz.sk7z.fastuse.command.PlayerConfigCommand;
import xyz.sk7z.fastuse.command.ServerStatusCommand;
import xyz.sk7z.fastuse.command.TimeSyncCommand;
import xyz.sk7z.fastuse.listener.*;
import xyz.sk7z.fastuse.player_options.PlayerOptions;

import java.util.HashMap;
import java.util.UUID;

public class FastUse extends PluginFrame {

    HashMap<UUID, PlayerOptions> playerValuesList = null;
    HashMap<UUID, PlayerEatManager> PlayerEatManagerList = null;

    @Override
    public void onEnable() {
        initialize();
        FastUseUtils.plugin = this;
        playerValuesList = new HashMap<>();
        PlayerEatManagerList = new HashMap<>();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        //レモン鯖向けカスタマイズ(使わない)
        //Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeSync(this), 100L, 5L);
    }

    @Override
    public void onDisable() {
        disable();
        getLogger().info(getName() + " Disable");
    }

    @Override
    public void initializeConfig() {
    }

    @Override
    public void initializeCommand() {
        CommandFrame cmd = new FastUseCommand(this, "fu");
        cmd.addCommand(new PlayerConfigCommand(this, "conf"));
        cmd.addCommand(new ServerStatusCommand(this, "status"));
        cmd.addCommand(new TimeSyncCommand(this, "timesync"));
        registerPluginCommand(cmd);
    }

    @Override
    public void initializeListener() {
        registerPluginListener(new AttackListener(this, "player"));
        registerPluginListener(new ShotBowListener(this, "player"));
        registerPluginListener(new ShotTridentListener(this, "player"));
        registerPluginListener(new FoodListener(this, "player"));
        registerPluginListener(new DrinkListener(this, "player"));
        registerPluginListener(new GlideListener(this, "player"));
        registerPluginListener(new PlayerHeadChangeListener(this, "player"));
        registerPluginListener(new ChairListener(this, "player"));
    }

    public PlayerOptions getPlayerValues(Player player) {
        if (!playerValuesList.containsKey(player.getUniqueId())) {
            playerValuesList.put(player.getUniqueId(), new PlayerOptions(this, player));
        }
        return playerValuesList.get(player.getUniqueId());
    }

    public PlayerEatManager getPlayerEatManager(Player player) {
        if (!PlayerEatManagerList.containsKey(player.getUniqueId())) {
            PlayerEatManagerList.put(player.getUniqueId(), new PlayerEatManager(this, player));
        }
        return PlayerEatManagerList.get(player.getUniqueId());
    }
}




