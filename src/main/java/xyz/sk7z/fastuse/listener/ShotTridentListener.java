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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FullChargeSound;
import xyz.sk7z.fastuse.player_options.PlayerShotOptions;

@SuppressWarnings("Duplicates")
public class ShotTridentListener extends ListenerFrame {

    private FastUse plg;

    public ShotTridentListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;


    }


    /* 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ */
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {
        if (true) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        PlayerShotOptions playerShotValues = plg.getPlayerValues(player).getPlayerShotTridentValues();


        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (playerShotValues.isEnabled()) {
                if (usedItem != null && isTrident(usedItem)) {

                    //見つからなければ
                    if (!playerShotValues.isAlreadyStarted()) {
                        playerShotValues.setStartTime();
                        playerShotValues.setStart_tick(player.getWorld().getTime());
                        new FullChargeSound(player, plg, playerShotValues).runTaskLater(plg, 5);
                    } else {
                        //120秒以上経過してたらやり直し
                        if (playerShotValues.getElapsedTimeMillis() >= 120 * 1000) {
                            playerShotValues.setStartTime();
                            new FullChargeSound(player, plg, playerShotValues).runTaskLater(plg, 1);
                        }
                    }
                }

            }
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            PlayerShotOptions shotValues = plg.getPlayerValues(player).getPlayerShotTridentValues();
            net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);
            if (usedItem != null && isTrident(usedItem)) {
                if (nmsItemStack.getItem() instanceof ItemTrident) {

                    ItemTrident nmsItemTrident = (ItemTrident) nmsItemStack.getItem();

                    player.sendMessage("チャージ時間" + shotValues.getElapsedTimeMillis());
                    nmsItemTrident.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle(), (int) (72000 - shotValues.getElapsedTimeMillis() / 50));
                    shotValues.setEndTime();
                }
            }

        }

    }


    private boolean isTrident(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemTrident;
    }

}
