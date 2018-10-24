package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;

public class GripperCommand extends Command
{
    private boolean finished = false;
    private Value value;

    public GripperCommand(Value value)
    {
        requires(Robot.getGripper());

        this.value = value;
    }
    
    public GripperCommand()
    {
    	requires(Robot.getGripper());
    }
    
    @Override
    protected void initialize()
    {
    	Robot.getGripper().setValue(value);
    	finished = true;
    }

    @Override
    protected boolean isFinished()
    {
        return finished;
    }
}
