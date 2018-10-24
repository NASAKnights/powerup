package xyz.nasaknights.powerup;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import xyz.nasaknights.powerup.commands.*;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;
import xyz.nasaknights.powerup.subsystems.*;

import java.util.Arrays;

public class Robot extends IterativeRobot
{
    private static final LogLevel MAX_LOG_LEVEL = LogLevel.DEBUG;
    private static final int PCM_CAN_PORT = 40;
    private static Joystick driver = new Joystick(0);
    private static Joystick operator = new Joystick(1);
    private static DrivetrainSubsystem drivetrain;
    private static IntakeSubsystem intake;
    private static WristSubsystem wrist;
    private static ElevatorSubsystem elevator;
    private static GripperSubsystem gripper;
    private static AHRS navx;
    private static DigitalInput dio0 = new DigitalInput(0);
    private static DigitalInput dio1 = new DigitalInput(1);
    private static DigitalInput dio2 = new DigitalInput(2);

    public static IntakeSubsystem getIntake()
    {
        return intake;
    }

    public static WristSubsystem getWrist()
    {
        return wrist;
    }

    public static GripperSubsystem getGripper()
    {
        return gripper;
    }

    public static DrivetrainSubsystem getDrivetrain()
    {
        return drivetrain;
    }

    public static LogLevel getMaxLevel()
    {
        return MAX_LOG_LEVEL;
    }

    public static int getPCMPort()
    {
        return PCM_CAN_PORT;
    }

    public static Joystick getDriver()
    {
        return driver;
    }

    public static Joystick getOperator()
    {
        return operator;
    }

    public static ElevatorSubsystem getElevator()
    {
        return elevator;
    }

    @Override
    public void robotInit()
    {
        long start = System.currentTimeMillis();

        Loggable.log("SYSTEM", LogLevel.INFO, "Welcome to the NASA Knights (FRC Team 122) PowerUp program, proudly developed and maintained by the following:");

        for (Authors a : Authors.values())
        {
            Loggable.log("CREDITS", LogLevel.INFO, a.toString());
        }

        Loggable.log("SYSTEM", LogLevel.INFO, "Your logging level is set to " + MAX_LOG_LEVEL.name() + ".");

        try
        {
            Loggable.log("Compressor", LogLevel.INFO, "Beginning compressor initialization. Please wait.");

            Compressor compressor = new Compressor(PCM_CAN_PORT);
            compressor.setClosedLoopControl(true);

            Loggable.log("Compressor", LogLevel.INFO, "Done.");
        } catch (Exception e)
        {
            Loggable.log("SYSTEM", LogLevel.WARNING, "The pneumatics compressor has failed to initialize, and the robot will not gain air pressure. The stacktrace is found below.");
            Loggable.log("Intake", LogLevel.WARNING, Arrays.toString(e.getStackTrace()));
        }

        Loggable.log("SYSTEM", LogLevel.INFO, "Beginning systems initialization. Please wait.");

        resetSubsystems();

        prepareInputs();
        
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        
        camera.setResolution(640, 480);

        Loggable.log("SYSTEM", LogLevel.INFO, "Initialization completed. Took " + (System.currentTimeMillis() - start) + " milliseconds.");
        
        try {
	          navx = new AHRS(SPI.Port.kMXP); 
	    } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
	    }
    }

    @Override
    public void disabledInit()
    {
    }

    @Override
    public void autonomousInit()
    {
        AutonomousCommand.Position position;

        if (dio0.get() && dio1.get())
            position = AutonomousCommand.Position.MIDDLE;
        else if (!dio0.get() && dio1.get())
            position = AutonomousCommand.Position.RIGHT;
        else
            position = AutonomousCommand.Position.LEFT;

        new AutonomousCommand(position, dio2.get());
        new WristCommand(Value.kReverse).start();
        new GripperCommand(Value.kForward).start();
    }

    @Override
    public void teleopInit()
    {
    	new ArcadeDriveCommand().start();
    }

    @Override
    public void testInit()
    {
    }

    @Override
    public void disabledPeriodic()
    {
    }

    @Override
    public void autonomousPeriodic()
    {
    }

    @Override
    public void teleopPeriodic()
    {
    }

    @Override
    public void testPeriodic()
    {
    }
    
    @Override
    public void robotPeriodic()
    {
    	Scheduler.getInstance().run();
    }

    private void resetSubsystems()
    {
        try
        {
            drivetrain = new DrivetrainSubsystem();
            intake = new IntakeSubsystem();
            wrist = new WristSubsystem();
            elevator = new ElevatorSubsystem();
            gripper = new GripperSubsystem();
            
            wrist.setWristValue(Value.kForward);
        } catch (SubsystemInitializationException e)
        {
            Loggable.log("SYSTEM", LogLevel.CRITICAL, "A subsystem has failed to initialize, and the robot will not function properly. The stacktrace is found below.");
            Loggable.log("SYSTEM", LogLevel.CRITICAL, Arrays.toString(e.getStackTrace()));
        }
    }

    private void prepareInputs()
    {
        new JoystickButton(getDriver(), PS4Controller.Buttons.TRIANGLE.getID()).whenPressed(new RumbleCommand(getOperator(), true));
        new JoystickButton(getDriver(), PS4Controller.Buttons.TRIANGLE.getID()).whenReleased(new RumbleCommand(getOperator(), false));
        new JoystickButton(getDriver(), PS4Controller.Buttons.RIGHT_BUMPER.getID()).whenPressed(new GripperCommand(Value.kReverse));
        new JoystickButton(getDriver(), PS4Controller.Buttons.RIGHT_BUMPER.getID()).whenReleased(new GripperCommand(Value.kForward));

        new JoystickButton(getOperator(), PS4Controller.Buttons.SQUARE.getID()).whenPressed(new WristCommand(Value.kForward));
        new JoystickButton(getOperator(), PS4Controller.Buttons.SQUARE.getID()).whenReleased(new WristCommand(Value.kReverse));
        new JoystickButton(getOperator(), PS4Controller.Buttons.TRIANGLE.getID()).whileHeld(new ElevatorCommand(true));
        new JoystickButton(getOperator(), PS4Controller.Buttons.CIRCLE.getID()).whileHeld(new ElevatorCommand(false));

        new JoystickButton(getOperator(), PS4Controller.Buttons.LEFT_BUMPER.getID()).whileHeld(new IntakeCommand(true, false));
        new JoystickButton(getOperator(), PS4Controller.Buttons.RIGHT_BUMPER.getID()).whileHeld(new IntakeCommand(false, false));
        new JoystickButton(getOperator(), PS4Controller.Buttons.OPTIONS.getID()).whileHeld(new IntakeCommand(false, true));
        new JoystickButton(getOperator(), PS4Controller.Buttons.SHARE.getID()).whileHeld(new IntakeCommand(true, true));
    }

    private enum Authors
    {
        BRADLEY("Bradley Hooten", "bradleyah02@gmail.com", "Lead Developer"),
        ANDREI("Andrei Stan", "astan54321@gmail.com", "Programming Mentor"),
        MIKE("Mike Byrd", "michael.dreher.byrd@gmail.com", "NASA Knights Team Lead");

        private String name;
        private String email;
        private String role;

        Authors(String name, String email, String role)
        {
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getName()
        {
            return name;
        }

        public String getEmail()
        {
            return email;
        }

        public String getRole()
        {
            return role;
        }

        @Override
        public String toString()
        {
            return getName() + ", " + getRole() + "; " + getEmail();
        }
    }
    
    public static AHRS getNavX()
    {
    	return navx;
    }
}