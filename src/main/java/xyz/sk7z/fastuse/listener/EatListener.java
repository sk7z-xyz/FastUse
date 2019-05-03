package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FastUseUtils;
import xyz.sk7z.fastuse.player_options.PlayerEatOptions;


public class EatListener extends ListenerFrame {

    private FastUse plg;

    public EatListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;


    }


    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PlayerEatOptions playerEatOptions = plg.getPlayerValues(player).getPlayerEatOptions();
        ItemStack usedItem = event.getItem();
        Block clickedBlock = event.getClickedBlock();
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        if (clickedBlock != null) {
            //もし種植目的なら無視
            if (FastUseUtils.isPlaceFoodSeed(clickedBlock, event.getItem()) || FastUseUtils.isOpenableBlock(clickedBlock)) {
                return;
            }

        }


        if (playerEatOptions.isEnabled()) {
            if (usedItem != null && isFood(usedItem) && (isHungry(player) || canSatietyEat(usedItem))) {
                event.setCancelled(true);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1f, 1);
                if (canEat(player)) {
                    if (nmsItemStack.getItem() instanceof ItemFood) {

                        ItemFood nmsItemFood = (ItemFood) nmsItemStack.getItem();
                        //ItemFoodクラスのbメソッドを参照して食べる
                        nmsItemFood.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle());

                        usedItem.setAmount(usedItem.getAmount() - 1);

                        //スープ用個別処理
                        if (nmsItemFood instanceof ItemSoup) {
                            player.getInventory().addItem(new ItemStack(Material.BOWL, 1));
                        }
                        playerEatOptions.setEndTime();


                    }

                }
            }

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();
        PlayerEatOptions playerEatOptions = plg.getPlayerValues(player).getPlayerEatOptions();
        if (playerEatOptions.isEnabled()) {
            if (isFood(event.getItem())) {
                //通常の食事はキャンセルする
                event.setCancelled(true);
            }
        }
    }

    private boolean isFood(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemFood;
    }


    private boolean isHungry(Player player) {
        return player.getFoodLevel() < 20;
    }

    //満腹でも食べられるアイテムか 英語わかんねー
    private boolean canSatietyEat(ItemStack item) {

        switch (item.getType()) {
            case GOLDEN_APPLE:
            case ENCHANTED_GOLDEN_APPLE:
            case CHORUS_FRUIT:
                return true;
        }
        return false;
    }


    private boolean canEat(Player player) {
        PlayerEatOptions playerEatValues = plg.getPlayerValues(player).getPlayerEatOptions();

        //食べ始めてから30秒立ってたら拒否
        if (playerEatValues.getElapsedTimeMillis() >= 30 * 1000) {
            playerEatValues.setEndTime();
            return false;
        }
        return playerEatValues.getElapsedTimeMillis() >= 1.6 * 1000;

    }




}
