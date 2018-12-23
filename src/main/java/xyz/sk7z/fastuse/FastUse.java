package xyz.sk7z.fastuse;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.command.FastUseCommand;
import xyz.sk7z.fastuse.listener.*;
import xyz.sk7z.fastuse.player_options.PlayerOptions;

import java.util.HashMap;
import java.util.UUID;

public class FastUse extends PluginFrame {

    HashMap<UUID, PlayerOptions> playerValuesList = null;

    @Override
    public void onEnable() {
        initialize();
        playerValuesList = new HashMap<>();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
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
        registerPluginCommand(cmd);
    }


    @Override
    public void initializeListener() {
        registerPluginListener(new AttackListener(this, "player"));
        registerPluginListener(new ShotBowListener(this, "player"));
        registerPluginListener(new EatListener(this, "player"));
        registerPluginListener(new DrinkListener(this, "player"));
        registerPluginListener(new GlideListener(this, "player"));
        registerPluginListener(new PlayerHeadChangeListener(this, "Player"));
        registerPluginListener(new ShotTridentListener(this,"Player"));

    }


    public PlayerOptions getPlayerValues(Player player) {
        if (!playerValuesList.containsKey(player.getUniqueId())) {
            playerValuesList.put(player.getUniqueId(), new PlayerOptions());
        }
        return playerValuesList.get(player.getUniqueId());
    }


}




