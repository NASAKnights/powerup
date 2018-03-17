package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand
{
    public AutonomousCommand(Position position)
    {
    	String gameMessage = DriverStation.getInstance().getGameSpecificMessage();
    	
    	new TurnToAngleCommand(-90).start();
    	
    	switch(position)
    	{
    		case LEFT:
    			
    			break;
    		case MIDDLE:
    			
    			break;
    		case RIGHT:
    			if(gameMessage.charAt(0) == 'R')
    			{
    				new Right_Switch_Auto().start();
    			}
    			else
    			{
    				new StraightDriveCommand(-.7, 2800).start();
    			}
    			break;
    	}
    }
    
    private final class Right_Switch_Auto extends CommandGroup
    {
    	public Right_Switch_Auto()
    	{
    		addSequential(new StraightDriveCommand(-.7, 2800), 2.8);
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.SWITCH));
            addSequential(new TurnToAngleCommand(-90));
            addSequential(new StraightDriveCommand(-.7, 1500));
    	}
    }
    
    public enum Position
    {
    	LEFT,
    	MIDDLE,
    	RIGHT;
    }
}
