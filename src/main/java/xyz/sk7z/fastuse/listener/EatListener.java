package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.ItemFood;
import net.minecraft.server.v1_13_R2.ItemSoup;
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

public class EatListener extends ListenerFrame {

    private HashMap<Player, Instant> player_eat_time_list = null;

    public EatListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        player_eat_time_list = new HashMap<>();

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
            if (usedItem != null && isFood(usedItem) && (isHungry(player) || canSatietyEat(usedItem))) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10, 1);
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

                        setEatEnd(player);

                    }

                }
            }

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {

        FastUseParam ep;
        if ((ep = ((FastUse) plg).getEatParamUser(event.getPlayer())) == null || ep.getOpt() == ON) {
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

        if (player_eat_time_list.containsKey(player)) {

            Instant eat_time = player_eat_time_list.get(player);
            //食べ始めてから30秒立ってたら拒否
            if (ChronoUnit.SECONDS.between(eat_time, Instant.now()) >= 30f) {
                setEatEnd(player);
                return false;
            }
            return ChronoUnit.SECONDS.between(eat_time, Instant.now()) >= 2f;
        } else {
            setEatStart(player);
            return false;
        }
    }


    private void setEatStart(Player player) {
        player_eat_time_list.put(player, Instant.now());
    }

    private void setEatEnd(Player player) {
        player_eat_time_list.remove(player);
    }
}
