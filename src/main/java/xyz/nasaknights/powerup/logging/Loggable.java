package xyz.nasaknights.powerup.logging;

import xyz.nasaknights.powerup.Robot;

import java.util.Date;
import java.util.TimeZone;

public abstract class Loggable
{
    public Loggable()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
    }

    public static void log(String name, LogLevel level, String message)
    {
        if (level.ordinal() < Robot.getMaxLevel().ordinal()) return;

        System.out.println("PowerUp Message <" + new Date() + "> (" + level.name() + ") [" + name + "] : " + message);
    }

    public abstract String getName();

    public abstract LogLevel getLevel();

    public abstract String getMessage();

    public final void log()
    {
        if (getLevel().ordinal() < Robot.getMaxLevel().ordinal()) return;

        System.out.println("PowerUp Message <" + new Date() + "> (" + getLevel().name() + ") [" + getName() + "] : " + getMessage());
    }
}
