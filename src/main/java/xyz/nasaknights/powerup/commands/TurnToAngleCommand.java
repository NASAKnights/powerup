package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import xyz.nasaknights.powerup.Robot;

public class TurnToAngleCommand extends PIDCommand {
	public TurnToAngleCommand(double target) {
    	super("TurnToAngle", .01, 0, 0);

        this.getPIDController().setSetpoint(target);
        this.getPIDController().setAbsoluteTolerance(2);
    }

    @Override
    protected void initialize() {
        this.getPIDController().enable();
    }

    @Override
    protected void end() {
        this.getPIDController().disable();
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected void interrupted() {
        this.getPIDController().disable();
        Robot.getDrivetrain().getDrive().arcadeDrive(0, 0);
    }

    @Override
    protected double returnPIDInput() {
        return Robot.getNavX().getAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.getDrivetrain().getDrive().arcadeDrive(0, output);
    }

    @Override
    protected boolean isFinished() {
        return this.getPIDController().onTarget();
    }
}
