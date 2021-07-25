package xyz.sk7z.fastuse;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sk7z.fastuse.player_options.PlayerFoodOptions;

import java.util.UUID;
import java.util.logging.Level;


public class PlayerEatManager {
    private FastUse plg;
    private UUID player_uuid;
    private ItemStack itemStack;
    private org.bukkit.scheduler.BukkitTask bukkitTask;


    public PlayerEatManager(FastUse plg, Player player) {
        this.plg = plg;
        this.player_uuid = player.getUniqueId();
    }

    public ItemStack getEatItem(){
        return this.itemStack;
    }

    //食事を開始する
    public void EatStart() {
        //Utl.sendPluginMessage(plg, player,"Eat Start");
        Player player = plg.getServer().getPlayer(player_uuid);
        ItemStack itemStack = FastUseUtils.getUsedFoodItemFromPlayer(player);

        if (itemStack == null || !(FastUseUtils.isFood(itemStack) || FastUseUtils.isDrink(itemStack))) {
            //基本来ないと思うが念の為｡
            return;
        }
        if (bukkitTask == null || bukkitTask.isCancelled()) {
            //タスクが存在しない場合､もしくは既にキャンセル済みの場合は､新しくタスクを作成する｡
            this.itemStack = itemStack;
            //Utl.sendPluginMessage(plg, player,"Timer is null or Cancelled");
            //Utl.sendPluginMessage(plg, player,"Timer Start");
            bukkitTask = (new PlayerEatScheduler(plg, player, this.itemStack)).runTaskTimer(plg, 1, 1);
        } else {
            //タスクが存在している場合
            if (this.itemStack.getType().equals(itemStack.getType())) {
                //Utl.sendPluginMessage(plg, player, "Timer is already started");
                //アイテムが同じなら既存のタスクを継続させる｡
                return;
            } else {
                //アイテムが異なるなら､新しいアイテムを選択する｡
                this.itemStack = itemStack;
                bukkitTask.cancel();
                //Utl.sendPluginMessage(plg, player, "Timer is already started but item changed");
                //Utl.sendPluginMessage(plg, player, "Timer Start");
                bukkitTask = (new PlayerEatScheduler(plg, player, this.itemStack)).runTaskTimer(plg, 1, 1);
            }

        }
    }

}

class PlayerEatScheduler extends BukkitRunnable {
    private FastUse plg;
    private Player player;
    private ItemStack eatItem;

    PlayerFoodOptions playerEatValues;

    PlayerEatScheduler(FastUse plg, Player player, ItemStack eatItem) {
        this.plg = plg;
        this.player = player;
        this.eatItem = eatItem;
        this.playerEatValues = plg.getPlayerValues(player).getPlayerFoodOptions();
        playerEatValues.setStartTime();
    }

    public void run() {
        try {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            if (craftPlayer == null) {
                //Utl.sendPluginMessage(plg, player,"Timer canceled: CraftPlayer is null");
                playerEatValues.setEndTime();
                cancel();
                return;
            }
            EntityLiving el = craftPlayer.getHandle();
            //右クリックが押下されていない場合はキャンセルする
            if (!el.isHandRaised()) {
                //Utl.sendPluginMessage(plg, player,"HandRaised is false");
                //食事イベントにより右クリックが解除された場合は対象外
                if(!playerEatValues.getSkipHandRaisedCheck()){
                    //Utl.sendPluginMessage(plg, player,"Timer canceled: HandRaised is false");
                    playerEatValues.setEndTime();
                    cancel();
                    return;
                }
                 //Utl.sendPluginMessage(plg,player,"SkipHandRaisedCheck");
            }
            ItemStack usedItem = FastUseUtils.getUsedFoodItemFromPlayer(player);
            if (!usedItem.getType().equals(eatItem.getType())) {
                //Utl.sendPluginMessage(plg, player,"Timer canceled: UsingItem Changed");
                playerEatValues.setEndTime();
                cancel();
                return;
            }
            if (!FastUseUtils.isDrink(usedItem) &&
                    (!FastUseUtils.isFood(usedItem) || (!FastUseUtils.isPlayerhungry(player) && !FastUseUtils.isSatietyFood(usedItem)))) {
                return;
            }
            if (FastUseUtils.canPlayerEatTime(player)) {
                //Utl.sendPluginMessage(plg, player,"Player can eat");
                net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);
                Item nmsItemFood = nmsItemStack.getItem();
                //Itemクラスのaメソッドを参照して食べる｡
                nmsItemFood.a(nmsItemStack, craftPlayer.getHandle().getWorld(), craftPlayer.getHandle());

                if (FastUseUtils.isDrink(usedItem)) {
                    player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                }
                if (FastUseUtils.isFoodSoup(usedItem)) {
                    player.getInventory().addItem(new ItemStack(Material.BOWL, 1));
                }
                usedItem.setAmount(usedItem.getAmount() - 1);

                playerEatValues.setEndTime();
                playerEatValues.setStartTime();
            }


        } catch (Exception e) {
            //何かしらの例外が起きた場合は､タイマーをキャンセルさせる｡
            plg.getLogger().log(Level.WARNING, e.toString());
            cancel();
        }

    }

}