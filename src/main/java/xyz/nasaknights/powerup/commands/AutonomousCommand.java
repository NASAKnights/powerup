package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup
{
    public AutonomousCommand()
    {
        addSequential(new StraightDriveCommand(-.7, 3000), 3);
    }
}
