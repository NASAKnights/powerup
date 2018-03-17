package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import xyz.nasaknights.powerup.Robot;

public class ElevatorCommand extends PIDCommand
{
    public ElevatorCommand(boolean up)
    {
        super("Elevator", .00015, 0, .00007);

        requires(Robot.getElevator());

        this.getPIDController().setSetpoint(up ? ElevatorHeight.TOP.getHeight() : ElevatorHeight.BOTTOM.getHeight());
        this.getPIDController().setAbsoluteTolerance(100);
        this.getPIDController().setOutputRange(-.35, .80);
    }

    @Override
    protected void end()
    {
        this.getPIDController().disable();
        Robot.getElevator().setPower(0.0);
    }

    @Override
    protected boolean isFinished() {
        return this.getPIDController().onTarget();
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
    protected void interrupted() {
        this.getPIDController().disable();
        Robot.getElevator().setPower(0.0);
    }

    @Override
    protected void initialize() {
        this.getPIDController().enable();
    }

    public enum ElevatorHeight
    {
        BOTTOM(500),
        SWITCH(4000),
        MIDDLE(10300),
        TOP(20800);

        private int height;

        ElevatorHeight(int height)
        {
            this.height = height;
        }

        public int getHeight()
        {
            return this.height;
        }
    }
}
