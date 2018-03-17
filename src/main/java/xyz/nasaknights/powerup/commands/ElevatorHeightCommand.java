package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import xyz.nasaknights.powerup.Robot;

public class ElevatorHeightCommand extends PIDCommand {
    public ElevatorHeightCommand(ElevatorCommand.ElevatorHeight target) {
    	super("Elevator", .00015, 0, .00007);

        this.getPIDController().setSetpoint(target.getHeight());
        this.getPIDController().setAbsoluteTolerance(1000);
        this.getPIDController().setOutputRange(-.35, .8);
    }

    @Override
    protected void initialize() {
        this.getPIDController().enable();
    }

    @Override
    protected void end() {
        this.getPIDController().disable();
        Robot.getElevator().setPower(0.0);
    }

    @Override
    protected void interrupted() {
        this.getPIDController().disable();
        Robot.getElevator().setPower(0.0);
    }

    @Override
    protected double returnPIDInput() {
        return Robot.getElevator().getPosition();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.getElevator().setPower(output);
    }

    @Override
    protected boolean isFinished() {
        return this.getPIDController().onTarget();
    }
}
