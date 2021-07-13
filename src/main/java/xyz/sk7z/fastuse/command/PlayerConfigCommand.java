
package xyz.sk7z.fastuse.command;


import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.CommandType;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.player_options.PlayerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * fastuse confコマンドクラス
 *
 * @author sk7z
 */
public class PlayerConfigCommand extends CommandFrame {

    FastUse plg;

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public PlayerConfigCommand(PluginFrame plg_, String name_) {
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
        return "fastuse.conf";
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
        if (!checkRange(sender, args, 1, 2)) return true;
        if (!(sender instanceof Player)) {
            Utl.sendPluginMessage(plg, sender, "コンソールやブロックからは実行できません");
            return true;
        }
        Player player = (Player) sender;
        PlayerOptions options = plg.getPlayerValues(player);


        CommandType commandType;
        try {
            commandType = CommandType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            Utl.sendPluginMessage(plg, player, "引数が異常です->" + args[0]);
            return true;
        }

        if (args.length == 1) {
            switch (commandType) {
                case FOOD:
                    Utl.sendPluginMessage(plg, player, "FOOD:" + options.getPlayerFoodOptions().isEnabled());
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
                case INFO:
                    Utl.sendPluginMessage(plg, player, "FOOD:" + options.getPlayerFoodOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "DRINK:" + options.getPlayerDrinkOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "GLIDE:" + options.getPlayerGlideOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "ATTACK:" + options.getPlayerAttackOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "BOW:" + options.getPlayerShotBowOptions().isEnabled());
                    Utl.sendPluginMessage(plg, player, "TRIDENT:" + options.getPlayerShotTridentOptions().isEnabled());
                    break;
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
            case FOOD:
                options.getPlayerFoodOptions().setEnabled(enabled);
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
            case "TRUE":
                enabled = true;
                break;
            case "FALSE":
                enabled = false;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return enabled;
    }

    /**
     * コマンド別タブコンプリート処理
     *
     * @param sender  コマンド送信者インスタンス
     * @param cmd     コマンドインスタンス
     * @param string  コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (CommandType commandType : CommandType.values()) {
                if (commandType.name().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(commandType.toString().toLowerCase());
                }
            }
        } else if (strings.length == 2) {
            if ("true".startsWith(strings[1].toLowerCase())) {
                list.add("true");
            }
            if ("false".startsWith(strings[1].toLowerCase())) {
                list.add("false");
            }
        }
        return list;
    }
}
