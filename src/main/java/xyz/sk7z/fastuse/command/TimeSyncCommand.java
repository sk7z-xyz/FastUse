package xyz.sk7z.fastuse.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.TimeSync;

import java.util.ArrayList;
import java.util.List;

/**
 * fastuse timeSyncコマンドクラス
 *
 * @author sk7z
 */
public class TimeSyncCommand extends CommandFrame {

    FastUse plg;

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public TimeSyncCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUse) plg_;
        setAuthBlock(true);
        setAuthConsole(true);

    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "fastuse.timesync";
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
        if (!checkRange(sender, args, 0, 1)) return true;

        if (args.length == 0) {
            Utl.sendPluginMessage(plg, sender, "TimeSync:" + TimeSync.isEnabled());
        } else {
            boolean enabled = false;
            try {
                enabled = isEnabledString(args[0]);
            } catch (IllegalArgumentException e) {
                Utl.sendPluginMessage(plg, sender, "引数が異常です->" + args[0]);
                return true;
            }
            TimeSync.setEnabled(enabled);
            Utl.sendPluginMessage(plg, sender, "設定を変更しました" + "TimeSync" + ":" + TimeSync.isEnabled());
        }
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
     * @param sender コマンド送信者インスタンス
     * @param cmd コマンドインスタンス
     * @param string コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            if("true".startsWith(strings[0].toLowerCase())){
                list.add("true");
            }
            if("false".startsWith(strings[0].toLowerCase())){
                list.add("false");
            }
        }
        return list;
    }

}
