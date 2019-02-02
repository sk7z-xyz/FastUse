package xyz.sk7z.fastuse;


import org.bukkit.plugin.Plugin;


import java.util.Calendar;


public class TimeSync implements Runnable {

    Plugin plg;

    public TimeSync(Plugin plg) {
        this.plg = plg;
    }

    @Override
    public void run() {

        Calendar calendar = Calendar.getInstance();
        long time = (calendar.get(Calendar.MILLISECOND) + calendar.get(Calendar.SECOND) * 1000 + (calendar.get(Calendar.MINUTE) % 20) * 60 * 1000) / 50;
        plg.getServer().getWorlds().forEach(world -> {
            if(world.getTime() < time){
                world.setTime(time);
            }
        });


    }
}
