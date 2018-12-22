package xyz.sk7z.fastuse.player_options;

public class PlayerDrinkOptions extends AbstractTimer implements Options {
    private boolean enabled = true;

    protected PlayerDrinkOptions() {
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
