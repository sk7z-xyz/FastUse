package xyz.sk7z.fastuse.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.CommandType;
import xyz.sk7z.fastuse.FastUse;
import xyz.sk7z.fastuse.FastUseParam;
import xyz.sk7z.fastuse.ToggleOptionType;

public class EatCommand extends CommandFrame {

    public EatCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "fastuse.eat";
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

        ToggleOptionType opt = null;

        Player player = (Player) sender;
        if (args.length >= 1) {
            try {
                opt = ToggleOptionType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        if (opt != null) {
            FastUseParam eat_param = new FastUseParam(player, CommandType.EAT, opt);
            ((FastUse) plg).setEatParamUser(eat_param);
            player.sendMessage(eat_param.getType() + ":" + eat_param.getOpt());
        } else {
            FastUseParam eat_param = ((FastUse) plg).getEatParamUser(player);
            player.sendMessage(eat_param.getType() + ":" + eat_param.getOpt());
        }

        return true;
    }
}
