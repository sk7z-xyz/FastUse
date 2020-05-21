package xyz.sk7z.fastuse.player_options;


import org.bukkit.entity.Player;
import xyz.sk7z.fastuse.FastUse;

public class PlayerOptions {
    private PlayerAttackOptions playerAttackOptions;
    private PlayerDrinkOptions playerDrinkOptions;
    private PlayerFoodOptions playerFoodOptions;
    private PlayerGlideOptions playerGlideOptions;
    private PlayerShotBowOptions playerShotBowOptions;
    private PlayerShotTridentOptions playerShotTridentOptions;
    private PlayerChairOptions playerChairOptions;



    public PlayerOptions(FastUse plg, Player player) {
        this.playerAttackOptions = new PlayerAttackOptions();
        this.playerDrinkOptions = new PlayerDrinkOptions();
        this.playerFoodOptions = new PlayerFoodOptions();
        this.playerGlideOptions = new PlayerGlideOptions();
        this.playerShotBowOptions = new PlayerShotBowOptions();
        this.playerShotTridentOptions = new PlayerShotTridentOptions();
        this.playerChairOptions = new PlayerChairOptions();
    }

    public PlayerAttackOptions getPlayerAttackOptions() {
        return playerAttackOptions;
    }

    public PlayerDrinkOptions getPlayerDrinkOptions() {
        return playerDrinkOptions;
    }

    public PlayerFoodOptions getPlayerFoodOptions() {
        return playerFoodOptions;
    }

    public PlayerGlideOptions getPlayerGlideOptions() {
        return playerGlideOptions;
    }

    public PlayerShotBowOptions getPlayerShotBowOptions() {
        return playerShotBowOptions;
    }

    public PlayerShotTridentOptions getPlayerShotTridentOptions() {
        return playerShotTridentOptions;
    }

    public PlayerChairOptions getPlayerChairOptions() {
        return playerChairOptions;
    }
}
