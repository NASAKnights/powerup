package xyz.nasaknights.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class ElevatorSubsystem extends Subsystem
{
    private final int FRONT_LEFT_ID = 5;
    private final int FRONT_RIGHT_ID = 7;
    private final int REAR_LEFT_ID = 6;
    private final int REAR_RIGHT_ID = 8;

    private final int TOP_LIMIT_ID = 0;
    private final int BOTTOM_LIMIT_ID = 1;

    private WPI_TalonSRX frontLeft;
    private WPI_TalonSRX frontRight;
    private WPI_TalonSRX rearLeft;
    private WPI_TalonSRX rearRight;

    private DigitalInput topLimitSwitch;
    private DigitalInput bottomLimitSwitch;

    public ElevatorSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Elevator", LogLevel.INFO, "Initializing elevator motors.");

            frontLeft = new WPI_TalonSRX(FRONT_LEFT_ID);
            frontRight = new WPI_TalonSRX(FRONT_RIGHT_ID);

            rearLeft = new WPI_TalonSRX(REAR_LEFT_ID);
            rearRight = new WPI_TalonSRX(REAR_RIGHT_ID);

            Loggable.log("Elevator", LogLevel.INFO, "Done.");
            Loggable.log("Elevator", LogLevel.INFO, "Configuring elevator motors.");

            rearRight.set(ControlMode.Follower, FRONT_RIGHT_ID);
            rearLeft.set(ControlMode.Follower, FRONT_LEFT_ID);

            frontLeft.configOpenloopRamp(.2, 10);
            frontRight.configOpenloopRamp(.2, 10);

            frontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Elevator initialization failed with " + e.getMessage());
        }
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    public void setPower(double power)
    {
        frontLeft.set(power);
        frontRight.set(power * -1);
    }

    public boolean getTopLimit()
    {
        return false; // topLimitSwitch.get();
    }

    public boolean getBottomLimit()
    {
        return false; // bottomLimitSwitch.get();
    }

    public int getPosition()
    {
        return frontLeft.getSensorCollection().getQuadraturePosition();
    }
}
