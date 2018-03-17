package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class ElevatorCommand extends Command
{
    private static final double kP = 1;

    private boolean up;

    private ElevatorHeight level = null;

    private boolean finished = false;

    public ElevatorCommand(boolean up)
    {
        requires(Robot.getElevator());
        this.up = up;
    }

    public ElevatorCommand(ElevatorHeight level)
    {
        requires(Robot.getElevator());
        this.level = level;
    }

    @Override
    protected void execute()
    {
    	if(up && Robot.getElevator().getTopLimit())
        {
        	Robot.getElevator().setPower(0);
        }
        else if(!up && Robot.getElevator().getBottomLimit())
        {
        	Robot.getElevator().setPower(0);
        }
        else
        {
        	Robot.getElevator().setPower(up ? .7 : -.35);
        }
    }
    
    @Override
    protected void end()
    {
    	Robot.getElevator().setPower(0.0);
    }

    @Override
    protected boolean isFinished()
    {
        return finished;
    }

    public enum ElevatorHeight
    {
        BOTTOM(0),
        SWITCH(4000),
        TOP(20600);

        private int height;

        ElevatorHeight(int height)
        {
            this.height = height;
        }

        public int getHeight()
        {
            return this.height;
        }
    }
}
