package xyz.sk7z.fastuse.player_options;


public class PlayerOptions {
    private PlayerAttackOptions playerAttackOptions;
    private PlayerDrinkOptions playerDrinkOptions;
    private PlayerEatOptions playerEatOptions;
    private PlayerGlideOptions playerGlideOptions;
    private PlayerShotBowOptions playerShotBowOptions;
    private PlayerShotTridentOptions playerShotTridentOptions;


    public PlayerOptions() {
        this.playerAttackOptions = new PlayerAttackOptions();
        this.playerDrinkOptions = new PlayerDrinkOptions();
        this.playerEatOptions = new PlayerEatOptions();
        this.playerGlideOptions = new PlayerGlideOptions();
        this.playerShotBowOptions = new PlayerShotBowOptions();
        this.playerShotTridentOptions = new PlayerShotTridentOptions();
    }

    public PlayerAttackOptions getPlayerAttackOptions() {
        return playerAttackOptions;
    }

    public PlayerDrinkOptions getPlayerDrinkOptions() {
        return playerDrinkOptions;
    }

    public PlayerEatOptions getPlayerEatOptions() {
        return playerEatOptions;
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
}
