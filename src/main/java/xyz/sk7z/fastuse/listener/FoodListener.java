package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FastUseUtils;
import xyz.sk7z.fastuse.player_options.PlayerFoodOptions;


public class FoodListener extends ListenerFrame {

    private FastUse plg;

    public FoodListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerFoodOptions playerFoodOptions = plg.getPlayerValues(player).getPlayerFoodOptions();
        ItemStack usedItem = event.getItem();
        Block clickedBlock = event.getClickedBlock();
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (clickedBlock != null) {
            //もし種植目的なら無視
            if (FastUseUtils.isPlaceFoodSeed(event.getItem(),clickedBlock) || FastUseUtils.isCanRightClockBlock(clickedBlock)) {
                return;
            }
        }
        if (playerFoodOptions.isEnabled()) {
            if (usedItem != null && FastUseUtils.isFood(usedItem) && (FastUseUtils.isPlayerhungry(player) || FastUseUtils.isSatietyFood(usedItem))) {
                plg.getPlayerEatManager(player).EatStart();

            }

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();
        PlayerFoodOptions playerFoodOptions = plg.getPlayerValues(player).getPlayerFoodOptions();
        if (playerFoodOptions.isEnabled()) {
            if (FastUseUtils.isFood(event.getItem())) {
                //通常の食事はキャンセルする
                event.setCancelled(true);
            }
        }
    }




}

