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
import xyz.sk7z.fastuse.FastUseParam;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static xyz.sk7z.fastuse.ToggleOptionType.ON;

@SuppressWarnings("Duplicates")
public class ShotListener extends ListenerFrame {

    private HashMap<Player, Instant> chargeStartTime_PlayerList = null;
    private HashMap<Player, Boolean> isPluginShot_PlayerList;


    public ShotListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        chargeStartTime_PlayerList = new HashMap<>();
        isPluginShot_PlayerList = new HashMap<>();

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerShotBowEvent(EntityShootBowEvent event) {


        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        //fastEatのときはイベント呼ばれなかったけど今回はイベントが呼ばれるので
        //1発目のノーマルで発射した矢は削除してその後プラグインで発射
        //2発目(プラグインでの発射)時には何もしない

        Player player = (Player) event.getEntity();
        if (isPluginShot_PlayerList.containsKey(player) && isPluginShot_PlayerList.get(player)) {
            //今回はプラグインからの発射なのでキャンセルしない
            //プラグインの発射もしない
            isPluginShot_PlayerList.put(player, false);
            return;
        } else {
            //マイクラ本来の発射はキャンセルする
            //その後プラグインで発射する
            isPluginShot_PlayerList.put(player, true);
            event.getProjectile().remove();
            //player.sendMessage("バニラの発射をキャンセルしました");

        }


        ItemStack usedItem = player.getInventory().getItemInMainHand();
        if (!isBow(usedItem)) {
            usedItem = player.getInventory().getItemInOffHand();
        }

        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);

        FastUseParam ep;


        if ((ep = ((FastUse) plg).getEatParamUser(player)) == null || ep.getOpt() == ON) {
            //多分この条件式いらないけど念の為
            if (usedItem != null && isBow(usedItem)) {
                if (nmsItemStack.getItem() instanceof ItemBow) {

                    ItemBow nmsItemBow = (ItemBow) nmsItemStack.getItem();
                    if (chargeStartTime_PlayerList.containsKey(player)) {

                        long ms = ChronoUnit.MILLIS.between(chargeStartTime_PlayerList.get(player), Instant.now());
                        setChargeEnd(player);
                        //player.sendMessage("チャージ時間" + ms);
                        nmsItemBow.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle(), 72000 - (int) ms / 50);
                    }


                }


            }

        }

    }


    /* 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ */
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する


        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        FastUseParam ep;

        if ((ep = ((FastUse) plg).getEatParamUser(player)) == null || ep.getOpt() == ON) {
            if (usedItem != null && isBow(usedItem)) {
                //見つからなければ
                if (!chargeStartTime_PlayerList.containsKey(player)) {
                    setChargeStart(player);
                    //player.sendMessage("チャージ開始");
                } else {
                    //120秒以上経過してたらやり直し
                    if (ChronoUnit.SECONDS.between(chargeStartTime_PlayerList.get(player), Instant.now()) >= 120f) {
                        setChargeStart(player);
                    }
                }
            }

        }

    }

    private void setChargeStart(Player player) {
        chargeStartTime_PlayerList.put(player, Instant.now());
    }

    private void setChargeEnd(Player player) {
        chargeStartTime_PlayerList.remove(player);
    }


    private boolean isBow(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemBow;
    }

}
