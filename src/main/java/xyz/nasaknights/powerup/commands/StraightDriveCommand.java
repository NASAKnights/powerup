package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import xyz.nasaknights.powerup.Robot;

public class StraightDriveCommand extends PIDCommand {
    private long timeout;
    private long startMillis = Long.MAX_VALUE;
    private double power;
    private double angle;

    public StraightDriveCommand(double power, long timeout) {
        super("Straight Drive", .007, 0, .0001);
        this.power = power;
        this.timeout = timeout;

        this.getPIDController().setOutputRange(0, .1);
        this.getPIDController().setAbsoluteTolerance(2);
    }

    @Override
    protected void initialize() {
        angle = Robot.getNavX().getAngle();

        this.getPIDController().setSetpoint(angle);
        this.getPIDController().enable();

        startMillis = System.currentTimeMillis();
    }

    @Override
    protected void interrupted()
    {
        this.getPIDController().disable();
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected void end() {
        this.getPIDController().disable();
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() > (startMillis + timeout);
    }

    @Override
    protected double returnPIDInput() {
        return Robot.getNavX().getAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.getDrivetrain().getDrive().arcadeDrive(power, output);
    }
}
