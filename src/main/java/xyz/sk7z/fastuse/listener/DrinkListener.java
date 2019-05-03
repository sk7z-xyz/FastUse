package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.ItemLingeringPotion;
import net.minecraft.server.v1_13_R2.ItemPotion;
import net.minecraft.server.v1_13_R2.ItemSplashPotion;
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
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);
        Block clickedBlock = event.getClickedBlock();

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (clickedBlock != null && FastUseUtils.isOpenableBlock(clickedBlock)) {
            return;
        }


        if (playerDrinkOptions.isEnabled()) {
            if (usedItem != null && isNormalPotion(usedItem)) {
                event.setCancelled(true);

                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
                if (canDrink(player)) {
                    if (nmsItemStack.getItem() instanceof ItemPotion) {

                        ItemPotion nmsItemPotion = (ItemPotion) nmsItemStack.getItem();
                        //ItemFoodクラスのbメソッドを参照して飲む
                        nmsItemPotion.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle());

                        usedItem.setAmount(usedItem.getAmount() - 1);

                        player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, 1));

                        playerDrinkOptions.setEndTime();

                    }

                }
            }

        }

    }


    private boolean isNormalPotion(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        if (nmsItemStack.getItem() instanceof ItemPotion) {
            //スプラッシュポーションと残留ポーションはfalse
            return !(nmsItemStack.getItem() instanceof ItemLingeringPotion) && !(nmsItemStack.getItem() instanceof ItemSplashPotion);

        }
        return false;
    }


    private boolean canDrink(Player player) {
        PlayerDrinkOptions playerDrinkOption = plg.getPlayerValues(player).getPlayerDrinkOptions();

        //飲み始めてから30秒立ってたら拒否
        if (playerDrinkOption.getElapsedTimeMillis() >= 30 * 1000) {
            playerDrinkOption.setEndTime();
            return false;
        }
        return playerDrinkOption.getElapsedTimeMillis() >= 1.6 * 1000;

    }
}
