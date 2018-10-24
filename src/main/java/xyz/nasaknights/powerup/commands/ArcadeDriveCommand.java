package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.PS4Controller;
import xyz.nasaknights.powerup.Robot;

public class ArcadeDriveCommand extends Command
{
    public ArcadeDriveCommand()
    {
        requires(Robot.getDrivetrain());
    }

    @Override
    protected void execute()
    {
        Robot.getDrivetrain().getDrive().arcadeDrive(Robot.getDriver().getRawAxis(PS4Controller.Axis.LEFT_Y.getID()), Robot.getDriver().getRawAxis(PS4Controller.Axis.RIGHT_X.getID()));
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
