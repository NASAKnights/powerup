package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.Joystick;
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
        Joystick controller = SupervisorCommand.isSupervisory() ? Robot.getOperator() : Robot.getDriver();

        Robot.getDrivetrain().getDrive().arcadeDrive(controller.getRawAxis(PS4Controller.Axis.LEFT_Y.getID()), controller.getRawAxis(PS4Controller.Axis.RIGHT_X.getID()));
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
