package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;

public class SupervisorCommand extends Command {
    private static boolean enabled = false;
    private boolean toEnable;
    private boolean isDone = false;

    public SupervisorCommand(boolean enable)
    {
        toEnable = enable;
    }

    @Override
    protected void execute() {
        if(Robot.getDemonstrationMode())
        {
            if(toEnable == enabled) return;

            Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);

            enabled = toEnable;
            isDone = true;
        }
        else
            isDone = true;
    }

    @Override
    protected boolean isFinished() {
        return isDone;
    }

    public static boolean isSupervisory()
    {
        return enabled;
    }
}
