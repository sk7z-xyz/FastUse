package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FastUseUtils;
import xyz.sk7z.fastuse.player_options.PlayerDrinkOptions;


public class DrinkListener extends ListenerFrame {

    FastUse plg;

    public DrinkListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;

    }


    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PlayerDrinkOptions playerDrinkOptions = plg.getPlayerValues(player).getPlayerDrinkOptions();
        ItemStack usedItem = event.getItem();
        Block clickedBlock = event.getClickedBlock();

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (clickedBlock != null && FastUseUtils.isCanRightClockBlock(clickedBlock)) {
            return;
        }

        if (playerDrinkOptions.isEnabled()) {
            if (usedItem != null && FastUseUtils.isDrink(usedItem)) {
                plg.getPlayerEatManager(player).EatStart();
            }
        }
    }
}
