package xyz.nasaknights.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import xyz.nasaknights.powerup.commands.ArcadeDriveCommand;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class DrivetrainSubsystem extends Subsystem
{
    private final int FRONT_LEFT_ID = 3;
    private final int FRONT_RIGHT_ID = 2;
    private final int REAR_LEFT_ID = 4;
    private final int REAR_RIGHT_ID = 1;

    private final double kP = 1;
    private final double kF = 1;

    private WPI_TalonSRX frontLeft;
    private WPI_TalonSRX frontRight;
    private WPI_TalonSRX rearLeft;
    private WPI_TalonSRX rearRight;

    private DifferentialDrive drive;

    public DrivetrainSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Drivetrain", LogLevel.INFO, "Initializing drivetrain motors.");

            frontLeft = new WPI_TalonSRX(FRONT_LEFT_ID);
            frontRight = new WPI_TalonSRX(FRONT_RIGHT_ID);

            rearLeft = new WPI_TalonSRX(REAR_LEFT_ID);
            rearRight = new WPI_TalonSRX(REAR_RIGHT_ID);

            Loggable.log("Drivetrain", LogLevel.INFO, "Done.");
            Loggable.log("Drivetrain", LogLevel.INFO, "Configuring drivetrain motors.");

            rearRight.set(ControlMode.Follower, FRONT_RIGHT_ID);
            rearLeft.set(ControlMode.Follower, FRONT_LEFT_ID);

            drive = new DifferentialDrive(frontLeft, frontRight);
            drive.setSafetyEnabled(false);

            Loggable.log("Drivetrain", LogLevel.INFO, "Done.");
            Loggable.log("Drivetrain", LogLevel.INFO, "Initializing drivetrain encoders.");

            rearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
            rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

            frontLeft.config_kP(0, kP, 10);
            frontLeft.config_kF(0, kF, 10);

            frontRight.config_kP(0, kP, 10);
            frontRight.config_kF(0, kF, 10);

            Loggable.log("Drivetrain", LogLevel.INFO, "Done.");
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Drivetrain initialization failed with " + e.getMessage());
        }
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ArcadeDriveCommand());
    }

    public DifferentialDrive getDrive()
    {
        return drive;
    }

    public WPI_TalonSRX getLeftMaster()
    {
        return frontLeft;
    }

    public WPI_TalonSRX getRightMaster()
    {
        return frontRight;
    }
}
