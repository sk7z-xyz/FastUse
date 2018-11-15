package xyz.sk7z.fastuse.Food;


public class Food {
    private String name;
    private int food_points;
    private float saturation_restored;

    public Food(String _name, int _food_points, float _saturation_restored) {
        this.name = _name;
        this.food_points = _food_points;
        this.saturation_restored = _saturation_restored;

    }

    public String getName() {
        return name;
    }

    public int getFood_points() {
        return food_points;
    }

    public float getSaturation_restored() {
        return saturation_restored;
    }
}
