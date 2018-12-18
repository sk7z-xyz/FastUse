package xyz.sk7z.fastuse.player_values;

public class PlayerShotValues extends abstractTimer {
    private int shot_count = 0;

    //PlayerValues以外にインスタンスを生成させないようにするためパッケージプライベートにする
    protected PlayerShotValues() {
    }

    public void addShotCount() {
        this.shot_count++;
    }

    public boolean isPluginShot() {
        return shot_count % 2 == 0;
    }
}
