package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;
import xyz.nasaknights.powerup.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command
{
    private boolean out;

    public IntakeCommand(boolean out)
    {
        this.out = out;
    }

    @Override
    protected void execute()
    {
        Robot.getIntake().setIntakeState(out ? IntakeSubsystem.IntakeState.EJECT : IntakeSubsystem.IntakeState.INTAKE, .9);
    }

    @Override
    protected void end()
    {
        Robot.getIntake().setIntakeState(IntakeSubsystem.IntakeState.STALL, 0);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
