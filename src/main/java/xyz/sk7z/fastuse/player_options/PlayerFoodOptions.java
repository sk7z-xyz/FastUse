package xyz.sk7z.fastuse.player_options;


public class PlayerFoodOptions extends AbstractTimer implements Options {
    private boolean enabled = true;
    private boolean skipHandRaisedCheck = false;

    protected PlayerFoodOptions() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public boolean getSkipHandRaisedCheck() {
        //trueの場合は1度目のみスキップする
        if(skipHandRaisedCheck){
            skipHandRaisedCheck = false;
            return true;
        }
        return false;
    }

    public void setSkipHandRaisedCheck() {
        this.skipHandRaisedCheck = true;
    }
}
