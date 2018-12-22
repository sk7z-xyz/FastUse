package xyz.sk7z.fastuse.player_options;

public class PlayerGlideOptions implements Options {
    private boolean enabled = true;

    protected PlayerGlideOptions() {
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
