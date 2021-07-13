package xyz.sk7z.fastuse.player_options;

import org.bukkit.entity.Arrow;

public class PlayerChairOptions implements Options {
    private boolean enabled = true;
    private Arrow arrow = null;

    protected PlayerChairOptions() {
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    public Arrow getArrow() {
        return arrow;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
