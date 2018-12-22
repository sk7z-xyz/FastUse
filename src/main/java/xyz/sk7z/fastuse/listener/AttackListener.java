package xyz.sk7z.fastuse.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.Lag;
import xyz.sk7z.fastuse.player_options.PlayerAttackOptions;


public class AttackListener extends ListenerFrame {
    private FastUse plg;

    public AttackListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        plg = (FastUse) plg_;

    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        PlayerAttackOptions playerAttackOptions = plg.getPlayerValues(player).getPlayerAttackOptions();
        if (playerAttackOptions.isEnabled()) {
            double attack_speed = 80 / Lag.getTPS();
            attack_speed = Math.max(attack_speed, 4);//最低は4(20TPS)
            attack_speed = Math.min(attack_speed, 20);//最大は20(5TPS)
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attack_speed);
        } else {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
    }


    @EventHandler(priority = EventPriority.LOW)
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
    }
}
