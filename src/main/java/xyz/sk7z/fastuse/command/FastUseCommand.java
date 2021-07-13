package xyz.sk7z.fastuse.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.command.CommandSender;

/**
 * fastuseコマンドクラス
 *
 * @author sk7z
 */
public class FastUseCommand extends CommandFrame {

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public FastUseCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
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
        Utl.sendTagMessage(plg, sender, plg.getName(), "パラメーターが不足しています");
        return true;
    }

}


