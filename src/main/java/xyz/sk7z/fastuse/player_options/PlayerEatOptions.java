package xyz.sk7z.fastuse.player_options;

public class PlayerEatOptions extends AbstractTimer implements Options {
    private boolean enabled = true;

    protected PlayerEatOptions() { }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
