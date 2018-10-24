package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;

public class TurnDriveCommand extends Command {
    private long timeout;
    private long startMillis = Long.MAX_VALUE;
    private double power;

    public TurnDriveCommand(double power, long timeout) {
        this.power = power;
        this.timeout = timeout;
    }

    @Override
    protected void initialize() {
        startMillis = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        Robot.getDrivetrain().getDrive().arcadeDrive(0, power);
    }

    @Override
    protected void interrupted()
    {
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected void end() {
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() > (startMillis + timeout);
    }
}
