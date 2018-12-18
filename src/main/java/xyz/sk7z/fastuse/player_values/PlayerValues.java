package xyz.sk7z.fastuse.player_values;


public class PlayerValues {
    private PlayerShotValues shotValues;
    private PlayerEatValues eatValues;
    private PlayerDrinkValues drinkValues;

    public PlayerValues() {
        this.shotValues = new PlayerShotValues();
        this.eatValues = new PlayerEatValues();
        this.drinkValues = new PlayerDrinkValues();
    }

    public PlayerShotValues getShotValues() {
        return shotValues;
    }

    public PlayerEatValues getEatValues() {
        return eatValues;
    }

    public PlayerDrinkValues getDrinkValues() {
        return drinkValues;
    }
}
