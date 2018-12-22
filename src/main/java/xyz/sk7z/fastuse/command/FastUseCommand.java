
package xyz.sk7z.fastuse.command;


import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.CommandType;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.player_options.PlayerOptions;

import java.util.Arrays;

/**
 * ece infoコマンドクラス
 *
 * @author ecolight
 */
public class FastUseCommand extends CommandFrame {

    FastUse plg;

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public FastUseCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;

    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "fastuse";
    }

    /**
     * 処理実行部
     *
     * @param sender コマンド送信者
     * @param args   パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerOptions options = plg.getPlayerValues(player);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("INFO")) {
                player.sendMessage("EAT:" + options.getPlayerEatOptions().isEnabled());
                player.sendMessage("DRINK:" + options.getPlayerDrinkOptions().isEnabled());
                player.sendMessage("GLIDE:" + options.getPlayerGlideOptions().isEnabled());
                player.sendMessage("ATTACK:" + options.getPlayerAttackOptions().isEnabled());
                player.sendMessage("BOW:" + options.getPlayerShotBowOptions().isEnabled());
                player.sendMessage("TRIDENT:" + options.getPlayerShotTridentValues().isEnabled());
                return true;
            }
        }

        if (args.length <= 2) {
            sender.sendMessage(("引数が足りません"));
            return true;
        }

        boolean enabled;

        try {
            enabled = isEnabledString(args[1]);
        } catch (IllegalArgumentException e) {
            player.sendMessage("引数が異常です->" + args[1]);
            return true;
        }

        switch (CommandType.valueOf(args[0].toUpperCase())) {
            case EAT:
                options.getPlayerEatOptions().setEnabled(enabled);
                break;
            case GLIDE:
                options.getPlayerGlideOptions().setEnabled(enabled);
                break;
            case DRINK:
                options.getPlayerDrinkOptions().setEnabled(enabled);
                break;
            case ATTACK:
                options.getPlayerAttackOptions().setEnabled(enabled);
                break;
            case BOW:
                options.getPlayerShotBowOptions().setEnabled(enabled);
                break;
            case TRIDENT:
                options.getPlayerShotTridentValues().setEnabled(enabled);
                break;
            default:
                player.sendMessage("引数が異常です" + args[0]);
                return true;

        }
        player.sendMessage("設定を変更しました" + CommandType.valueOf(args[0].toUpperCase()) + ":" + isEnabledString(args[1]));

        return true;
    }

    private boolean isEnabledString(String str) {
        boolean enabled;
        switch (str.toUpperCase()) {
            case "1":
            case "ON":
            case "TRUE":
            case "ENABLED":
                enabled = true;
                break;
            case "0":
            case "OFF":
            case "FALSE":
            case "DISABLED":
                enabled = false;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return enabled;
    }


}
