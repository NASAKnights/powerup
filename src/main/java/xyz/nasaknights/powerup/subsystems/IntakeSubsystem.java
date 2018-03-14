package xyz.nasaknights.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import xyz.nasaknights.powerup.logging.LogLevel;
import xyz.nasaknights.powerup.logging.Loggable;

public class IntakeSubsystem
{
    private final static int LEFT_CAN_ID = 9;
    private final static int RIGHT_CAN_ID = 10;

    private WPI_VictorSPX left;
    private WPI_VictorSPX right;

    public IntakeSubsystem() throws SubsystemInitializationException
    {
        try
        {
            Loggable.log("Intake", LogLevel.DEBUG, "Initializing intake motors, please wait.");

            left = new WPI_VictorSPX(LEFT_CAN_ID);
            right = new WPI_VictorSPX(RIGHT_CAN_ID);

            setIntakeState(IntakeState.STALL, 0);

            Loggable.log("Intake", LogLevel.DEBUG, "Done.");
        } catch (Exception e)
        {
            throw new SubsystemInitializationException("Wrist initialization failed with " + e.getMessage());
        }
    }

    public void setIntakeState(IntakeState state, double power)
    {
        Loggable.log("Intake", LogLevel.DEBUG, "Changing intake state to " + state.name() + " with power " + (state == IntakeState.STALL ? "15%" : "") + ".");

        switch (state)
        {
            case EJECT:
                left.set(power);
                right.set(power * -1);
                break;
            case INTAKE:
                left.set(power * -1);
                right.set(power);
                break;
            case STALL:
                left.set(-.15);
                right.set(.15);
                break;
        }
    }

    public enum IntakeState
    {
        INTAKE,
        EJECT,
        STALL;
    }
}
