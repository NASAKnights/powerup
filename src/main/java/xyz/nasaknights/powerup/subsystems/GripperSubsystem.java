package xyz.nasaknights.powerup.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.commands.GripperCommand;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class GripperSubsystem extends Subsystem
{
    private final static int FORWARD_PCM_ID = 2;
    private final static int REVERSE_PCM_ID = 3;
    
    private static long lastRun = 0;

    private DoubleSolenoid gripper;

    public GripperSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Gripper", LogLevel.DEBUG, "Initializing gripper pneumatics, please wait.");

            gripper = new DoubleSolenoid(Robot.getPCMPort(), FORWARD_PCM_ID, REVERSE_PCM_ID);
            
            gripper.set(Value.kForward);

            Loggable.log("Gripper", LogLevel.DEBUG, "Done.");
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Gripper initialization failed with " + e.getMessage());
        }
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    public void setSolenoidClosed(boolean closed)
    {
    	if(System.currentTimeMillis() - lastRun < 50)
    	{
    		return;
    	}
        gripper.set(closed ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        lastRun = System.currentTimeMillis();
    }
    
    public void setValue(Value value)
    {
    	if(System.currentTimeMillis() - lastRun < 50)
    	{
    		return;
    	}
    	
        gripper.set(value);
        lastRun = System.currentTimeMillis();
    }

    public boolean getGripperClosed()
    {
        return gripper.get() == DoubleSolenoid.Value.kReverse;
    }
}
