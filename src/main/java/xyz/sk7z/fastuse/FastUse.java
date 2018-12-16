package xyz.sk7z.fastuse;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.command.EatCommand;
import xyz.sk7z.fastuse.command.FastUseCommand;
import xyz.sk7z.fastuse.command.GlideCommand;
import xyz.sk7z.fastuse.listener.AttackListener;
import xyz.sk7z.fastuse.listener.EatListener;
import xyz.sk7z.fastuse.listener.GlideListener;
import xyz.sk7z.fastuse.listener.PlayerHeadChangeListener;

import java.util.HashMap;

public class FastUse extends PluginFrame {

    private static Player getter = null;
    HashMap<Player, FastUseParam> eatList = null;
    HashMap<Player, FastUseParam> glideList = null;

    @Override
    public void onEnable() {
        initialize();
        eatList = new HashMap<>();
        glideList = new HashMap<>();
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
        cmd.addCommand(new EatCommand(this, "eat"));
        cmd.addCommand(new GlideCommand(this, "glide"));
        registerPluginCommand(cmd);
    }


    @Override
    public void initializeListener() {
        registerPluginListener(new AttackListener(this, "player"));
        registerPluginListener(new EatListener(this, "player"));
        registerPluginListener(new GlideListener(this, "player"));
        registerPluginListener(new PlayerHeadChangeListener(this,"Player"));
    }



    public void setGetter(Player pl) {
        this.getter = pl;
    }

    public Player getGetter() {
        return this.getter;
    }

    public void setEatParamUser(FastUseParam param) {
        eatList.remove(param.getPlayer());
        eatList.put(param.getPlayer(), param);
    }

    public FastUseParam getEatParamUser(Player pl) {
        return eatList.get(pl);
    }

    public void setGlideParamUser(FastUseParam param) {
        glideList.remove(param.getPlayer());
        glideList.put(param.getPlayer(), param);
    }

    public FastUseParam getGlideParamUser(Player pl) {
        return glideList.get(pl);
    }


}




