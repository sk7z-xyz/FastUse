package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadChangeListener extends ListenerFrame {

    public PlayerHeadChangeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerHeadChange(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target_entity = event.getRightClicked();
        Player target_player;
        if (target_entity instanceof Player) {
            target_player = (Player) target_entity;
        } else {
            return;
        }
        ItemStack item_skull;
        if (player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD) {
            item_skull = player.getInventory().getItemInMainHand();
        } else {
            return;
        }
        playerHeadChange(item_skull, target_player);


    }

    public ItemStack playerHeadChange(ItemStack item_skull, Player player) {
        SkullMeta skull_meta = (SkullMeta) item_skull.getItemMeta();
        skull_meta.setDisplayName(player.getName() + " の頭");
        skull_meta.setOwningPlayer(player);
        item_skull.setItemMeta(skull_meta);
        return item_skull;

    }

}
