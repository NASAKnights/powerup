package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class AutonomousCommand
{
    public AutonomousCommand(Position position, boolean scale)
    {
        StraightDriveCommand basic = new StraightDriveCommand(-.7, 2800);
    	char[] gameMessage = DriverStation.getInstance().getGameSpecificMessage().toCharArray();

        Loggable.log("Autonomous", LogLevel.INFO, "Running " + position.name() + " auto with game message " + DriverStation.getInstance().getGameSpecificMessage());

    	switch(position)
    	{
    		case LEFT:
    			if(gameMessage[0] == 'L' && !scale)
                {
                    new Left_Switch_Auto().start();
                }
                else if(gameMessage[1] == 'L' && scale)
                {
                    new Left_Scale_Auto().start();
                }
                else if(gameMessage[1] == 'R' && gameMessage[0] == 'L' && scale)
                {
                    new Left_Switch_Auto().start();
                }
                else if(gameMessage[0] == 'R' && gameMessage[1] == 'L' && !scale)
                {
                    new Left_Scale_Auto().start();
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
                if(gameMessage[0] == 'R' && !scale)
                {
                    new Right_Switch_Auto().start();
                }
                else if(gameMessage[1] == 'R' && scale)
                {
                    new Right_Scale_Auto().start();
                }
                else if(gameMessage[1] == 'L' && gameMessage[0] == 'R' && scale)
                {
                    new Right_Switch_Auto().start();
                }
                else if(gameMessage[0] == 'L' && gameMessage[1] == 'R' && !scale)
                {
                    new Right_Scale_Auto().start();
                }
                else
                {
                    basic.start();
                }
    			break;
    	}
    }

    private final class Right_Switch_Auto extends CommandGroup
    {
        public Right_Switch_Auto()
        {
            addSequential(new StraightDriveCommand(-.7, 2600));
            addSequential(new DelayCommand(1000));
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.SWITCH, true));
            addSequential(new TurnDriveCommand(-.85, 750));
            addSequential(new StraightDriveCommand(-.7, 2000));
            addSequential(new IntakeCommand(true, false), .5);
        }
    }

    private final class Left_Switch_Auto extends CommandGroup
    {
        public Left_Switch_Auto()
        {
            addSequential(new StraightDriveCommand(-.7, 2600));
            addSequential(new DelayCommand(1000));
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.SWITCH, true));
            addSequential(new TurnDriveCommand(.85, 750));
            addSequential(new StraightDriveCommand(-.7, 2000));
            addSequential(new IntakeCommand(true, false), .5);
        }
    }

    private final class Right_Scale_Auto extends CommandGroup
    {
        public Right_Scale_Auto()
        {
            addSequential(new StraightDriveCommand(-.9, 1900), 1.9);
            addSequential(new StraightDriveCommand(-.7, 1000), 1);
            addSequential(new DelayCommand(300));
            addSequential(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.TOP, false), 1.5);
            addSequential(new TurnDriveCommand(-.85, 500), .5);
            addSequential(new StraightDriveCommand(-.7, 1000), 1);
            addSequential(new IntakeCommand(true, false), .3);
            addSequential(new DelayCommand(100));
            addSequential(new StraightDriveCommand(.65, 300), .3);
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.BOTTOM, false), 2.1);
            addSequential(new StraightDriveCommand(.7, 1500), 1.5);
        }
    }

    private final class Left_Scale_Auto extends CommandGroup
    {
        public Left_Scale_Auto()
        {
            addSequential(new StraightDriveCommand(-.9, 1900), 1.9);
            addSequential(new StraightDriveCommand(-.7, 1000), 1.0);
            addSequential(new DelayCommand(300));
            addSequential(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.TOP, false), 1.5);
            addSequential(new TurnDriveCommand(.85, 690), .69);
            addSequential(new StraightDriveCommand(-.7, 400), .4);
            addSequential(new DelayCommand(200));
            addSequential(new IntakeCommand(true, false), .3);
            addSequential(new DelayCommand(100));
            addSequential(new StraightDriveCommand(.65, 500), .5);
            addParallel(new ElevatorHeightCommand(ElevatorCommand.ElevatorHeight.BOTTOM, false), 2.1);
            addSequential(new StraightDriveCommand(.65, 700), .7);
        }
    }
    
    public enum Position
    {
    	LEFT,
    	MIDDLE,
    	RIGHT;
    }
}
