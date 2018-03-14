package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class GripperCommand extends Command
{
    private boolean finished = false;
    private boolean close;

    public GripperCommand(boolean close)
    {
        requires(Robot.getGripper());

        this.close = close;
    }

    @Override
    protected void execute()
    {
        if (Robot.getGripper().getGripperClosed() == close)
        {
            Loggable.log("Gripper", LogLevel.INFO, "Gripper already " + (close ? "closed" : "open") + ". Exiting command.");
            finished = true;
            return;
        }

        Robot.getGripper().setSolenoidClosed(close);
    }

    @Override
    protected boolean isFinished()
    {
        return finished;
    }
}
