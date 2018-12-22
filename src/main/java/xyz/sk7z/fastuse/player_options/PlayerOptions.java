package xyz.sk7z.fastuse.player_options;


public class PlayerOptions {
    private PlayerAttackOptions playerAttackOptions;
    private PlayerDrinkOptions playerDrinkOptions;
    private PlayerEatOptions playerEatOptions;
    private PlayerGlideOptions playerGlideOptions;
    private PlayerShotOptions playerShotBowOptions;
    private PlayerShotOptions playerShotTridentValues;


    public PlayerOptions() {
        this.playerAttackOptions = new PlayerAttackOptions();
        this.playerDrinkOptions = new PlayerDrinkOptions();
        this.playerEatOptions = new PlayerEatOptions();
        this.playerGlideOptions = new PlayerGlideOptions();
        this.playerShotBowOptions = new PlayerShotOptions();
        this.playerShotTridentValues = new PlayerShotOptions();
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

    public PlayerShotOptions getPlayerShotBowOptions() {
        return playerShotBowOptions;
    }

    public PlayerShotOptions getPlayerShotTridentValues() {
        return playerShotTridentValues;
    }
}
