package xyz.sk7z.fastuse.player_options;

public class PlayerAttackOptions implements Options {
    private boolean enabled = true;

    protected PlayerAttackOptions() {
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
