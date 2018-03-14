package xyz.nasaknights.powerup.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class WristSubsystem extends Subsystem
{
    private final static int FORWARD_PCM_ID = 2;
    private final static int REVERSE_PCM_ID = 3;

    private DoubleSolenoid wrist;

    public WristSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Wrist", LogLevel.DEBUG, "Initializing wrist pneumatics, please wait.");

            wrist = new DoubleSolenoid(Robot.getPCMPort(), FORWARD_PCM_ID, REVERSE_PCM_ID);

            Loggable.log("Wrist", LogLevel.DEBUG, "Done.");
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Wrist initialization failed with " + e.getMessage());
        }
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    public boolean getWristDown()
    {
        return !(wrist.get() == DoubleSolenoid.Value.kReverse);
    }

    public void setWristDown(boolean down)
    {
        wrist.set(down ? DoubleSolenoid.Value.kOff : DoubleSolenoid.Value.kForward);
    }
}
