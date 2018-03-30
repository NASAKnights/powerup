package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class AutonomousCommand
{
    public AutonomousCommand(Position position)
    {
        StraightDriveCommand basic = new StraightDriveCommand(-.7, 2800);
    	char[] gameMessage = DriverStation.getInstance().getGameSpecificMessage().toCharArray();

        Loggable.log("Autonomous", LogLevel.INFO, "Running " + position.name() + " auto with game message " + DriverStation.getInstance().getGameSpecificMessage());

    	switch(position)
    	{
    		case LEFT:
    			if(gameMessage[0] == 'L')
                {
                    Loggable.log("Autonomous", LogLevel.INFO, "LBA start");
                    new Left_Basic_Auto().start();
                }
                else
                {
                    basic.start();
                }
    			break;
    		case MIDDLE:
    			basic.start();
    			break;
    		case RIGHT:
    			if(gameMessage[0] == 'R')
    			{
                    Loggable.log("Autonomous", LogLevel.INFO, "RBA start");
    				new Right_Basic_Auto().start();
    			}
    			else
    			{
    				basic.start();
    			}
    			break;
    	}
    }

    private final class Right_Basic_Auto extends CommandGroup
    {
        public Right_Basic_Auto()
        {
            addSequential(new StraightDriveCommand(-.7, 2600));
            addSequential(new DelayCommand(1000));
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.SWITCH, true));
            addSequential(new TurnDriveCommand(-.85, 790));
            addSequential(new StraightDriveCommand(-.7, 2000));
            addSequential(new IntakeCommand(true), .5);
        }
    }

    private final class Left_Basic_Auto extends CommandGroup
    {
        public Left_Basic_Auto()
        {
            addSequential(new StraightDriveCommand(-.7, 2600));
            addSequential(new DelayCommand(1000));
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.SWITCH, true));
            addSequential(new TurnDriveCommand(.85, 790));
            addSequential(new StraightDriveCommand(-.7, 2000));
            addSequential(new IntakeCommand(true), .5);
        }
    }
    
    public enum Position
    {
    	LEFT,
    	MIDDLE,
    	RIGHT;
    }
}
