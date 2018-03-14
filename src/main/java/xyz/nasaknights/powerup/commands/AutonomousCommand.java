package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup
{
    private String gameData = DriverStation.getInstance().getGameSpecificMessage();

    public AutonomousCommand()
    {

    }
}
