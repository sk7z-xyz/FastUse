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
        //player.sendMessage(event.getDamage() + "");
        if (playerAttackOptions.isEnabled()) {
            final double DEFAULT_SWORD_ATTACK_SPEED = 1.6;
            final double DEFAULT_PLAYER_ATTACK_SPEED = 4;
            double player_attack_speed = (DEFAULT_SWORD_ATTACK_SPEED * (20f / Lag.getTPS())) + DEFAULT_PLAYER_ATTACK_SPEED - DEFAULT_SWORD_ATTACK_SPEED;
            /*
            1.6*(20/tps)+(4-1.6)
            TPS20のとき

             = 1.6(20/tps)+2.4
             = 1.6 * 1 + 2.4
             = 4 (プレイヤー攻撃速度)
             4 - (4 - 1.6)
             = 4 - 2.4
             = 1.6(剣の攻撃速度

             TPS10のとき
             = 1.6(20/tps)+2.4
             = 1.6 (20/10) + 2.4
             = 1.6 * 2 + 2.4
             = 5.6 (プレイヤー攻撃速度)
             5.6 - (4 - 1.6)
             = 5.6 - 2.4
             = 3.2(剣の攻撃速度
            */

            player_attack_speed = Math.max(player_attack_speed, 4);//最低は4(20TPS)
            player_attack_speed = Math.min(player_attack_speed, 10);//最大は20(5TPS)
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(player_attack_speed);

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
