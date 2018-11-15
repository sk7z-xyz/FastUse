package xyz.sk7z.fastuse.Food;


import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

public class FoodList {
    public HashMap<String, Food> foodlists;
    Plugin plg = null;

    public FoodList(Plugin _plg) {
        this.plg = _plg;
        foodlists = new HashMap<>();

        InputStream is = plg.getResource("food.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str;
        try {
            while ((str = br.readLine()) != null) {
                try {
                    String[] tmp = str.split(",");
                    String name = tmp[0];
                    int food_points = Integer.valueOf(tmp[1]);
                    float saturation_restored = Float.valueOf(tmp[2]);
                    foodlists.put(name, new Food(name, food_points, saturation_restored));
                    plg.getLogger().log(Level.WARNING, name + foodlists.get(name).getFood_points() + "");

                } catch (NumberFormatException e) {

                    plg.getLogger().log(Level.WARNING, "food.csvの書式設定が不正です");
                    plg.getLogger().log(Level.WARNING, e.toString());
                    plg.getLogger().log(Level.WARNING, str);

                }
            }
        } catch (IOException e) {
            plg.getLogger().log(Level.WARNING, "food.csvの読み込みに失敗");
        }
    }

    public boolean isFood(Material material) {
        return foodlists.containsKey(material + "");
    }

    public Food getFood(Material material) {
        if (!isFood(material)) {
            return null;
        }

        return foodlists.get(material + "");
    }
}
