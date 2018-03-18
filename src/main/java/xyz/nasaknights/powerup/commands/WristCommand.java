package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;

public class WristCommand extends Command
{
    private boolean finished = false;
    private Value value;

    public WristCommand(Value value)
    {
        requires(Robot.getWrist());
        
        this.value = value;
    }
    
    public WristCommand()
    {
    	requires(Robot.getWrist());
    	
    	if(Robot.getWrist().getWristValue() == Value.kForward)
    	{
    		value = Value.kOff;
    	}
    	else
    	{
    		value = Value.kForward;
    	}
    }

    @Override
    protected void execute()
    {
        Robot.getWrist().setWristValue(value);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
