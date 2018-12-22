package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.ItemBow;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FullChargeSound;
import xyz.sk7z.fastuse.player_options.PlayerShotOptions;


@SuppressWarnings("Duplicates")
public class ShotBowListener extends ListenerFrame {

    private FastUse plg;


    public ShotBowListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;


    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerShotBowEvent(EntityShootBowEvent event) {


        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PlayerShotOptions playerShotBowOptions = plg.getPlayerValues(player).getPlayerShotBowOptions();
        if (!playerShotBowOptions.isEnabled()) {
            return;
        }

        //fastEatのときはイベント呼ばれなかったけど今回はイベントが呼ばれるので
        //1発目のノーマルで発射した矢は削除してその後プラグインで発射
        //2発目(プラグインでの発射)時には何もしない

        playerShotBowOptions.addShotCount();

        if (playerShotBowOptions.isPluginShot()) {
            //今回はプラグインからの発射なのでキャンセルしない
            //プラグインの発射もしない
            return;
        } else {
            //マイクラ本来の発射はキャンセルする
            //その後プラグインで発射する
            event.getProjectile().remove();
            //player.sendMessage("バニラの発射をキャンセルしました");
        }


        ItemStack usedItem = player.getInventory().getItemInMainHand();
        if (!isBow(usedItem)) {
            usedItem = player.getInventory().getItemInOffHand();
        }

        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);


        if (usedItem != null && isBow(usedItem)) {
            if (nmsItemStack.getItem() instanceof ItemBow) {

                ItemBow nmsItemBow = (ItemBow) nmsItemStack.getItem();

                //player.sendMessage("チャージ時間" + shotValues.getElapsedTimeMillis());
                nmsItemBow.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle(), (int) (72000 - playerShotBowOptions.getElapsedTimeMillis() / 50));

                playerShotBowOptions.setEndTime();

            }
        }
    }


    /* 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ */
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PlayerShotOptions playerShotBowOptions = plg.getPlayerValues(player).getPlayerShotBowOptions();
        ItemStack usedItem = event.getItem();


        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        if (playerShotBowOptions.isEnabled()) {
            if (usedItem != null && isBow(usedItem)) {
                //見つからなければ
                if (!playerShotBowOptions.isAlreadyStarted()) {
                    playerShotBowOptions.setStartTime();
                    playerShotBowOptions.setStart_tick(player.getWorld().getTime());
                    if (playerShotBowOptions.isSoundEnabled()) {
                        new FullChargeSound(player, plg, playerShotBowOptions).runTaskLater(plg, 5);
                    }
                } else {
                    //120秒以上経過してたらやり直し
                    if (playerShotBowOptions.getElapsedTimeMillis() >= 120 * 1000) {
                        playerShotBowOptions.setStartTime();
                        if (playerShotBowOptions.isSoundEnabled()) {
                            new FullChargeSound(player, plg, playerShotBowOptions).runTaskLater(plg, 5);
                        }
                    }
                }
            }

        }

    }


    private boolean isBow(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemBow;
    }

}

