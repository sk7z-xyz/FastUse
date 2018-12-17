package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.ItemPotion;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import xyz.sk7z.fastuse.FastUseParam;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static xyz.sk7z.fastuse.ToggleOptionType.ON;

@SuppressWarnings("Duplicates")
public class DrinkListener extends ListenerFrame {

    private HashMap<Player, Instant> player_drink_time_list = null;

    public DrinkListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        player_drink_time_list = new HashMap<>();

    }


    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        FastUseParam ep;


        if ((ep = ((FastUse) plg).getEatParamUser(player)) == null || ep.getOpt() == ON) {
            if (usedItem != null && isPotion(usedItem)) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 10, 1);
                if (canDrink(player)) {
                    if (nmsItemStack.getItem() instanceof ItemPotion) {

                        ItemPotion nmsItemPotion = (ItemPotion) nmsItemStack.getItem();
                        //ItemFoodクラスのbメソッドを参照して飲む
                        nmsItemPotion.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle());

                        usedItem.setAmount(usedItem.getAmount() - 1);

                        player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, 1));

                        setDrinkEnd(player);

                    }

                }
            }

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {

        FastUseParam ep;
        if ((ep = ((FastUse) plg).getEatParamUser(event.getPlayer())) == null || ep.getOpt() == ON) {
            if (isPotion(event.getItem())) {
                //通常の食事はキャンセルする
                event.setCancelled(true);
            }
        }
    }

    private boolean isPotion(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemPotion;
    }


    private boolean canDrink(Player player) {

        if (player_drink_time_list.containsKey(player)) {

            Instant eat_time = player_drink_time_list.get(player);
            //食べ始めてから30秒立ってたら拒否
            if (ChronoUnit.SECONDS.between(eat_time, Instant.now()) >= 30f) {
                setDrinkEnd(player);
                return false;
            }
            return ChronoUnit.SECONDS.between(eat_time, Instant.now()) >= 2;
        } else {
            setDrinkStart(player);
            return false;
        }
    }


    private void setDrinkStart(Player player) {
        player_drink_time_list.put(player, Instant.now());
    }

    private void setDrinkEnd(Player player) {
        player_drink_time_list.remove(player);
    }
}
