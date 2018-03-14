package xyz.nasaknights.powerup.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.commands.GripperCommand;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class GripperSubsystem extends Subsystem
{
    private final static int FORWARD_PCM_ID = 0;
    private final static int REVERSE_PCM_ID = 1;

    private DoubleSolenoid gripper;

    public GripperSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Gripper", LogLevel.DEBUG, "Initializing gripper pneumatics, please wait.");

            gripper = new DoubleSolenoid(Robot.getPCMPort(), FORWARD_PCM_ID, REVERSE_PCM_ID);

            Loggable.log("Gripper", LogLevel.DEBUG, "Done.");
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Gripper initialization failed with " + e.getMessage());
        }
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new GripperCommand(true));
    }

    public void setSolenoidClosed(boolean closed)
    {
        gripper.set(closed ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }

    public boolean getGripperClosed()
    {
        return gripper.get() == DoubleSolenoid.Value.kReverse;
    }
}
