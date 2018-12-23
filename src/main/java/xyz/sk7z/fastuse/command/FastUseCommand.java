
package xyz.sk7z.fastuse.command;


import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.CommandType;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.player_options.PlayerOptions;

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

        if (args.length == 0) {
            Utl.sendPluginMessage(plg, player, ("/fastuse [EAT,DRINK,GLIDE,ATTACK,BOW,TRIDENT,SOUND] [ON,OFF]"));
            return true;
        }

        CommandType commandType;
        try {
            commandType = CommandType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            Utl.sendPluginMessage(plg, player, "引数が異常です->" + args[0]);
            return true;
        }

        if (args.length == 1) {
            switch (commandType) {
                case EAT:
                    Utl.sendPluginMessage(plg, player, "EAT:" + options.getPlayerEatOptions().isEnabled());
                    break;
                case DRINK:
                    Utl.sendPluginMessage(plg, player, "DRINK:" + options.getPlayerDrinkOptions().isEnabled());
                    break;
                case GLIDE:
                    Utl.sendPluginMessage(plg, player, "GLIDE:" + options.getPlayerGlideOptions().isEnabled());
                    break;
                case ATTACK:
                    Utl.sendPluginMessage(plg, player, "ATTACK:" + options.getPlayerAttackOptions().isEnabled());
                    break;
                case BOW:
                    Utl.sendPluginMessage(plg, player, "BOW:" + options.getPlayerShotBowOptions().isEnabled());
                    break;
                case TRIDENT:
                    Utl.sendPluginMessage(plg, player, "TRIDENT:" + options.getPlayerShotTridentOptions().isEnabled());
                    break;
                case SOUND:
                    Utl.sendPluginMessage(plg, player, "SOUND:" + options.getPlayerShotBowOptions().isSoundEnabled());
                    break;
                case INFO:
                    Utl.sendPluginMessage(plg, player, "EAT:" + options.getPlayerEatOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "DRINK:" + options.getPlayerDrinkOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "GLIDE:" + options.getPlayerGlideOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "ATTACK:" + options.getPlayerAttackOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "BOW:" + options.getPlayerShotBowOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "TRIDENT:" + options.getPlayerShotTridentOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "SOUND:" + options.getPlayerShotBowOptions().isSoundEnabled());
                    break;
                case STATUS:
                    ServerStatusCommand.info(plg,player);
            }
            return true;
        }


        boolean enabled;

        try {
            enabled = isEnabledString(args[1]);
        } catch (IllegalArgumentException e) {
            Utl.sendPluginMessage(plg, player, "引数が異常です->" + args[1]);
            return true;
        }

        switch (commandType) {
            case EAT:
                options.getPlayerEatOptions().setEnabled(enabled);
                break;
            case DRINK:
                options.getPlayerDrinkOptions().setEnabled(enabled);
                break;
            case GLIDE:
                options.getPlayerGlideOptions().setEnabled(enabled);
                break;
            case ATTACK:
                options.getPlayerAttackOptions().setEnabled(enabled);
                break;
            case BOW:
                options.getPlayerShotBowOptions().setEnabled(enabled);
                break;
            case TRIDENT:
                options.getPlayerShotTridentOptions().setEnabled(enabled);
                break;
            case SOUND:
                options.getPlayerShotBowOptions().setSoundEnabled(enabled);
                options.getPlayerShotTridentOptions().setSoundEnabled(enabled);
                break;
            default:
                Utl.sendPluginMessage(plg, player, "引数が異常です->" + commandType);
                return true;
        }
        Utl.sendPluginMessage(plg, player, "設定を変更しました" + commandType + ":" + enabled);

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
