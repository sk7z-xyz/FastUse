package xyz.sk7z.fastuse.player_options;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class AbstractTimer {

    Instant charge_start_time;

    protected AbstractTimer() {
    }

    public Long getElapsedTimeMillis() {
        if (!isAlreadyStarted()) {
            setStartTime();
        }
        return ChronoUnit.MILLIS.between(charge_start_time, Instant.now());
    }

    public boolean isAlreadyStarted() {
        return charge_start_time != null;
    }

    public void setStartTime() {
        this.charge_start_time = Instant.now();
    }

    public void setEndTime() {
        charge_start_time = null;
    }
}
