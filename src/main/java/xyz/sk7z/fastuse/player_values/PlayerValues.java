package xyz.sk7z.fastuse.player_values;


public class PlayerValues {
    private PlayerShotValues arrowShotValues;
    private PlayerShotValues tridentShotValues;
    private PlayerEatValues eatValues;
    private PlayerDrinkValues drinkValues;

    public PlayerValues() {
        this.arrowShotValues = new PlayerShotValues();
        this.tridentShotValues = new PlayerShotValues();
        this.eatValues = new PlayerEatValues();
        this.drinkValues = new PlayerDrinkValues();
    }

    public PlayerShotValues getArrowShotValues() {
        return arrowShotValues;
    }
    public PlayerShotValues getTridentShotValues() {
        return tridentShotValues;
    }

    public PlayerEatValues getEatValues() {
        return eatValues;
    }

    public PlayerDrinkValues getDrinkValues() {
        return drinkValues;
    }
}
