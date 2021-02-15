package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_15_R1.FoodInfo;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
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
            if (FastUseUtils.isPlaceFoodSeed(event.getItem(), clickedBlock) || FastUseUtils.isCanRightClockBlock(clickedBlock)) {
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
        ItemStack useItem = event.getItem();
        PlayerFoodOptions playerFoodOptions = plg.getPlayerValues(player).getPlayerFoodOptions();
        if (playerFoodOptions.isEnabled()) {
            if (FastUseUtils.isFood(useItem)) {

                //アイテムが1つの場合FastUseの機能で食べれなくなってしまうためイベントをキャンセルする
                if (useItem.getAmount() <= 1) {
                    event.setCancelled(true);
                    return;
                }
                //吐き出せるほど食料ゲージが溜まっているかチェックして､吐けない場合はFastUseの機能で食べさせる｡
                if(!checkVomiting(player,useItem)){
                    event.setCancelled(true);
                    return;
                }

                //満腹度を減らしてから食べる(無限むしゃむしゃ対策)
                vomiting(player, useItem);
                //次のTickで食べたアイテム返却(同じTickでアイテムをいじると同期が不正になる為)
                (new UndoItem(player, useItem)).runTaskLater(plg, 1);
                playerFoodOptions.setSkipHandRaisedCheck();
            }
        }
    }

    //吐き出す
    private void vomiting(Player player, ItemStack it) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(it);
        FoodInfo foodInfo = nmsItemStack.getItem().getFoodInfo();
        player.setFoodLevel(Math.max(0, player.getFoodLevel() - foodInfo.getNutrition()));
        player.setSaturation(Math.max(player.getFoodLevel(),
                player.getSaturation() - foodInfo.getNutrition() * foodInfo.getSaturationModifier() * 2));
    }
    //吐き出せるほど食料ゲージがあるか
    private Boolean checkVomiting(Player player, ItemStack it) {
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(it);
        FoodInfo foodInfo = nmsItemStack.getItem().getFoodInfo();
        //プレイヤーの満腹度 - 食品の満腹度 >= 0 なら満腹ゲージを減らしてマイクラの機能で減らした分を食べさせる｡
        //5-5 = 0 == true
        //5-6 = -1 == false
        return player.getFoodLevel() - foodInfo.getNutrition() >= 0;


    }


    class UndoItem extends BukkitRunnable {
        Player player;
        ItemStack itemStack;

        UndoItem(Player p, ItemStack it) {
            player = p;
            itemStack = it.clone();
            itemStack.setAmount(1);
        }

        @Override
        public void run() {
            //念の為チェック
            if(FastUseUtils.isFood(itemStack)) {
                player.getInventory().addItem(itemStack);
            }
        }
    }

}

