
package xyz.sk7z.fastuse;

import org.bukkit.entity.Player;

/**
 * @author ecolight
 */
public class FastUseParam {
    private Player pl = null;
    private CommandType type = null;
    private ToggleOptionType opt = null;


    public FastUseParam(Player pl, CommandType type, ToggleOptionType opt) {
        this.pl = pl;
        this.type = type;
        this.opt = opt;
    }

    public Player getPlayer() {
        return this.pl;
    }

    public CommandType getType() {
        return this.type;
    }

    public ToggleOptionType getOpt() {
        return this.opt;
    }


}
