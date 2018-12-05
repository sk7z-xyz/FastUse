
package xyz.sk7z.fastuse.listener;


import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.*;
import org.bukkit.Material;
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
import xyz.sk7z.fastuse.FastUseParam;
import xyz.sk7z.fastuse.FastUse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static xyz.sk7z.fastuse.ToggleOptionType.ON;

/**
 * プレイヤー系イベントリスナクラス
 *
 * @author ecolight
 */
public class PlayerListener extends ListenerFrame {

    private HashMap<Player, Instant> player_eat_time_list = null;


    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        player_eat_time_list = new HashMap<>();


    }


    /**
     * プレイヤー作用イベントハンドラ
     *
     * @param event
     */


    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {
        FastUseParam ep;
        if ((ep = ((FastUse) plg).getEatParamUser(event.getPlayer())) == null || ep.getOpt() == ON) {
            if (isFood(event.getItem())) {
                event.getPlayer().sendMessage("キャンセル");
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack chestPlate_Item = player.getInventory().getChestplate();
        ItemStack usedItem = event.getItem();
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);


        //右クリック以外は無視

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        //player.sendMessage("イベントが呼ばれた" + new Date());
        //player.chat("FoodLevel:"+player.getFoodLevel());
        //player.chat("SaturationLevel:"+player.getSaturation());

        FastUseParam gp;
        if ((gp = ((FastUse) plg).getGlideParamUser(player)) == null || gp.getOpt() == ON)
            //エリトラを開いていなくてかつ 空中にいる場合のみ実行する
            if (!player.isGliding() && !player.isOnGround()) {
                if (chestPlate_Item != null && usedItem != null) {
                    if (player.getInventory().getChestplate().getType() == Material.ELYTRA && event.getItem().getType() == Material.FIREWORK_ROCKET) {
                        Location l = player.getLocation();
                        player.teleport(l);
                        //player.chat("----エリトラ展開----");
                        player.setGliding(true);
                    }
                }
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
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10, 2);
                        endEat(player);

                    }

                }
            }

        }


    }


    private boolean isFood(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemFood;
    }


    private boolean isHungry(Player player) {
        return player.getFoodLevel() < 20;
    }

    //満腹でも食べられるか 英語わかんねー
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
                endEat(player);
                return false;
            }
            return ChronoUnit.SECONDS.between(eat_time, Instant.now()) >= 2f;
        } else {
            setEatStartTime(player);
            return false;
        }
    }

    private void setEatStartTime(Player player) {
        player_eat_time_list.put(player, Instant.now());
    }

    private void endEat(Player player) {
        player_eat_time_list.remove(player);
    }


}
