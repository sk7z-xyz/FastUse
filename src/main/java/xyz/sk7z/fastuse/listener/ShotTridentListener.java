package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_13_R2.ItemTrident;
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
import xyz.sk7z.fastuse.FastUseParam;
import xyz.sk7z.fastuse.player_values.PlayerShotValues;

import static xyz.sk7z.fastuse.ToggleOptionType.ON;

@SuppressWarnings("Duplicates")
public class ShotTridentListener extends ListenerFrame {

    private FastUse plg;

    public ShotTridentListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;


    }


    /* EntityShootTridentEventが呼ばれたら追加*/
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerShotBowEvent(EntityShootBowEvent event) {


        if(true){
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PlayerShotValues shotValues = plg.getPlayerValues(player).getTridentShotValues();

        //fastEatのときはイベント呼ばれなかったけど今回はイベントが呼ばれるので
        //1発目のノーマルで発射した矢は削除してその後プラグインで発射
        //2発目(プラグインでの発射)時には何もしない


        shotValues.addShotCount();
        if (shotValues.isPluginShot()) {
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
        if (!isTrident(usedItem)) {
            usedItem = player.getInventory().getItemInOffHand();
        }

        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);

        FastUseParam ep;


        if ((ep = ((FastUse) plg).getEatParamUser(player)) == null || ep.getOpt() == ON) {
            //多分この条件式いらないけど念の為
            if (usedItem != null && isTrident(usedItem)) {
                if (nmsItemStack.getItem() instanceof ItemTrident) {

                    ItemTrident nmsItemTrident = (ItemTrident) nmsItemStack.getItem();


                    player.sendMessage("チャージ時間" + shotValues.getElapsedTimeMillis());
                    //nmsItemTrident.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle(), (int) (72000 - shotValues.getElapsedTimeMillis() / 50));
                    shotValues.setEndTime();


                }


            }

        }

    }


    /* 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ */
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        PlayerShotValues playerShotValues = plg.getPlayerValues(player).getTridentShotValues();


        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        FastUseParam ep;

        if ((ep = (plg).getEatParamUser(player)) == null || ep.getOpt() == ON) {
            if (usedItem != null && isTrident(usedItem)) {

                //見つからなければ
                if (!playerShotValues.isAlreadyStarted()) {
                    playerShotValues.setStartTime();
                    playerShotValues.setStart_tick(player.getWorld().getTime());
                    new FullChargeSound(player, plg).runTaskLater(plg, 5);
                } else {
                    //120秒以上経過してたらやり直し
                    if (playerShotValues.getElapsedTimeMillis() >= 120 * 1000) {
                        playerShotValues.setStartTime();
                        new FullChargeSound(player, plg).runTaskLater(plg, 1);
                    }
                }
            }

        }

    }


    private boolean isTrident(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemTrident;
    }

}
