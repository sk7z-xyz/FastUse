
package xyz.sk7z.fastuse.listener;


import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.Food.Food;
import xyz.sk7z.fastuse.Food.FoodList;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;


/**
 * プレイヤー系イベントリスナクラス
 *
 * @author ecolight
 */
public class PlayerListener extends ListenerFrame {

    private HashMap<Player, Instant> player_eat_time_list = null;
    private FoodList foodList = null;


    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        player_eat_time_list = new HashMap<>();
        foodList = new FoodList(plg);


    }


    /**
     * プレイヤー作用イベントハンドラ
     *
     * @param event
     */

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerItemConsume(PlayerItemConsumeEvent event) {
        if (foodList.isFood(event.getItem().getType()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack chestplate_Item = player.getInventory().getChestplate();
        ItemStack usedItem = event.getItem();
        //player.sendMessage("イベントが呼ばれた" + new Date());

        if (chestplate_Item != null && usedItem != null) {
            if (player.getInventory().getChestplate().getType() == Material.ELYTRA && event.getItem().getType() == Material.FIREWORK_ROCKET) {
                Location l = player.getLocation();
                if (!player.isOnGround()) {
                    player.teleport(l);
                }
                player.setGliding(true);
            }
        }


        if (usedItem != null && (foodList.isFood(usedItem.getType()))) {

            if (isHungry(player)) {

                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10, 1);

                if (canEat(player)) {


                    Food food = foodList.getFood(usedItem.getType());

                    int old_FoodLevel = player.getFoodLevel();
                    int new_FoodLevel = old_FoodLevel + food.getFood_points();
                    if (new_FoodLevel >= 20) {
                        new_FoodLevel = 20;
                    }

                    float old_SaturationLevel = player.getSaturation();
                    float new_SaturationLevel = old_SaturationLevel + food.getSaturation_restored();
                    if (new_SaturationLevel >= 20f) {
                        new_SaturationLevel = 20f;
                    }

                    player.setFoodLevel(new_FoodLevel);
                    player.setSaturation(new_SaturationLevel);
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10, 2);

                    usedItem.setAmount(usedItem.getAmount() - 1);

                    endEat(player);
                }

            }

        }


    }

    public boolean isHungry(Player player) {
        return player.getFoodLevel() < 20;
    }


    public boolean canEat(Player player) {
        if (!isHungry(player)) {
            return false;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10, 1);
        if (player_eat_time_list.containsKey(player)) {
            Instant eat_time = player_eat_time_list.get(player);
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

    public void setEatStartTime(Player player) {
        player_eat_time_list.put(player, Instant.now());
    }

    public void endEat(Player player) {
        player_eat_time_list.remove(player);
    }


}
