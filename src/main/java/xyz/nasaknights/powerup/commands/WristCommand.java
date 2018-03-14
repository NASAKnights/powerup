package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class WristCommand extends Command
{
    private boolean finished = false;
    private boolean down;

    public WristCommand(boolean down)
    {
        requires(Robot.getWrist());

        this.down = down;
    }

    @Override
    protected void execute()
    {
        if (Robot.getWrist().getWristDown() == down)
        {
            Loggable.log("Wrist", LogLevel.INFO, "Wrist already " + (down ? "down" : "up") + ". Exiting command.");
            finished = true;
            return;
        }

        Robot.getWrist().setWristDown(down);
    }

    @Override
    protected boolean isFinished()
    {
        return finished;
    }
}
