package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.world.item.ItemTrident;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FullChargeSound;
import xyz.sk7z.fastuse.Lag;
import xyz.sk7z.fastuse.player_options.PlayerShotTridentOptions;

@SuppressWarnings("Duplicates")
public class ShotTridentListener extends ListenerFrame {

    private FastUse plg;

    public ShotTridentListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;
    }

    // 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        PlayerShotTridentOptions playerShotTridentOptions = plg.getPlayerValues(player).getPlayerShotTridentOptions();


        if (playerShotTridentOptions.isEnabled()) {
            if (usedItem != null && isTrident(usedItem)) {

                //見つからなければ
                if (!playerShotTridentOptions.isAlreadyStarted()) {
                    playerShotTridentOptions.setStartTime();
                    playerShotTridentOptions.setStart_tick(Lag.getTickCount());
                    new FullChargeSound(player, plg, playerShotTridentOptions).runTaskLater(plg, 5);

                } else {
                    //120秒以上経過してたらやり直し
                    if (playerShotTridentOptions.getElapsedTimeMillis() >= 120 * 1000) {
                        playerShotTridentOptions.setStartTime();
                        new FullChargeSound(player, plg, playerShotTridentOptions).runTaskLater(plg, 1);

                    }
                }
            }

        }
        /*
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            AbstractPlayerShotOptions shotValues = plg.getPlayerValues(player).getPlayerShotTridentOptions();
            net.minecraft.server.v1_17_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);
            if (usedItem != null && isTrident(usedItem)) {
                if (nmsItemStack.getItem() instanceof ItemTrident) {

                    ItemTrident nmsItemTrident = (ItemTrident) nmsItemStack.getItem();

                    player.sendMessage("チャージ時間" + shotValues.getElapsedTimeMillis());
                    nmsItemTrident.a(nmsItemStack, ((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getHandle(), (int) (72000 - shotValues.getElapsedTimeMillis() / 50));
                    shotValues.setEndTime();
                }
            }

        }
        */
    }


    private boolean isTrident(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof ItemTrident;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void ProjectileLaunch(ProjectileLaunchEvent event) {

        Player player;

        if (event.getEntity().getShooter() instanceof Player) {
            player = (Player) event.getEntity().getShooter();
        } else {
            return;
        }
        PlayerShotTridentOptions playerShotTridentOptions = plg.getPlayerValues(player).getPlayerShotTridentOptions();
        playerShotTridentOptions.setEndTime();
    }
}

