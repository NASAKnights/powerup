package xyz.nasaknights.powerup.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class WristSubsystem extends Subsystem
{
    private final static int FORWARD_PCM_ID = 0;
    private final static int REVERSE_PCM_ID = 1;
    
    private static long lastRun = 0;

    private DoubleSolenoid wrist;

    public WristSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Wrist", LogLevel.DEBUG, "Initializing wrist pneumatics, please wait.");

            wrist = new DoubleSolenoid(Robot.getPCMPort(), FORWARD_PCM_ID, REVERSE_PCM_ID);
            
            wrist.set(Value.kReverse);

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

    public Value getWristValue()
    {
        return wrist.get();
    }

    public void setWristValue(Value value)
    {
    	if(System.currentTimeMillis() - lastRun < 50)
    	{
    		return;
    	}
    	
        wrist.set(value);
        lastRun = System.currentTimeMillis();
    }
}
