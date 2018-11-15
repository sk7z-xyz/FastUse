package xyz.sk7z.fastuse;

import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.listener.PlayerListener;

public class FaseUse extends PluginFrame {

    private static Player getter = null;
    
    @Override
    public void onEnable() {
        initialize();
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

    }


    @Override
    public void initializeListener() {
        registerPluginListener(new PlayerListener(this, "player"));
    }


    public void setGetter(Player pl) {
        this.getter = pl;
    }

    public Player getGetter() {
        return this.getter;
    }






}




