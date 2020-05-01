package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.server.v1_15_R1.NBTBase;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadChangeListener extends ListenerFrame {

    public PlayerHeadChangeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }


    //Headの情報を表示する
    //コマンド用意するのがめんどかった()
    @EventHandler(priority = EventPriority.LOW)
    public void playerHeadInfo(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item_skull;
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (block.getType() != Material.CARROTS) {
            return;
        }
        if (event.getItem().getType() == Material.PLAYER_HEAD) {
            item_skull = event.getItem();
        } else {
            return;
        }
        event.setCancelled(true);
        net.minecraft.server.v1_15_R1.ItemStack nms_item_skull = CraftItemStack.asNMSCopy(item_skull);
        NBTBase skull_Owner = nms_item_skull.getTag() != null ? nms_item_skull.getTag().get("SkullOwner") : null;
        if (skull_Owner != null) {
            player.sendMessage(skull_Owner.toString());
        }


    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerHeadChange(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target_entity = event.getRightClicked();
        Player target_player;
        if(player.isSneaking()){
            return;
        }
        if (target_entity instanceof Player) {
            target_player = (Player) target_entity;
        } else {
            return;
        }
        ItemStack item_skull;
        if (player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD) {
            item_skull = player.getInventory().getItemInMainHand();
            playerHeadChange(item_skull, target_player);

        }


    }

    public ItemStack playerHeadChange(ItemStack item_skull, Player player) {
        SkullMeta skull_meta = (SkullMeta) item_skull.getItemMeta();
        skull_meta.setOwningPlayer(player);
        item_skull.setItemMeta(skull_meta);

        return item_skull;

    }

}
