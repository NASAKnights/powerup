package xyz.nasaknights.powerup.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team254.lib.trajectory.SrxMotionProfile;
import com.team254.lib.trajectory.SrxTrajectory;
import com.team254.lib.trajectory.SrxTrajectoryImporter;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import xyz.nasaknights.powerup.Robot;


public class FollowTrajectoryCommand extends Command
{

    private String trajectoryName = "";
    private int kMinPointsInTalon = 5;

    private boolean isFinished = false;

    private SrxTrajectory trajectoryToFollow = null;
    private SrxTrajectoryImporter importer = new SrxTrajectoryImporter("/home/lvuser/paths");

    private MotionProfileStatus rightStatus = new MotionProfileStatus();
    private MotionProfileStatus leftStatus = new MotionProfileStatus();

    private boolean hasPathStarted;

    /**
     * this is only either Disable, Enable, or Hold. Since we'd never want one
     * side to be enabled while the other is disabled, we'll use the same status
     * for both sides.
     */
    private SetValueMotionProfile setValue = SetValueMotionProfile.Disable;
    // Runs the runnable
    private Notifier loadLeftBuffer;
    private Notifier loadRightBuffer;

    // constructor
    public FollowTrajectoryCommand(String trajectoryName)
    {
        requires(Robot.getDrivetrain());
        this.trajectoryName = trajectoryName;
    }

    public FollowTrajectoryCommand(SrxTrajectory trajectoryToFollow)
    {
        requires(Robot.getDrivetrain());
        this.trajectoryToFollow = trajectoryToFollow;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {

        setUpTalon(Robot.getDrivetrain().getLeftMaster());
        setUpTalon(Robot.getDrivetrain().getRightMaster());


        setValue = SetValueMotionProfile.Disable;

        Robot.getDrivetrain().getLeftMaster().set(ControlMode.MotionProfile, setValue.value);
        Robot.getDrivetrain().getRightMaster().set(ControlMode.MotionProfile, setValue.value);

        if (trajectoryToFollow == null)
        {

            this.trajectoryToFollow = importer.importSrxTrajectory(trajectoryName);
        }

        int pidfSlot = 0;

        loadLeftBuffer = new Notifier(new BufferLoader(Robot.getDrivetrain().getRightMaster(), this.trajectoryToFollow.rightProfile, pidfSlot, true));
        loadRightBuffer = new Notifier(new BufferLoader(Robot.getDrivetrain().getLeftMaster(), this.trajectoryToFollow.leftProfile, pidfSlot, false));

        loadLeftBuffer.startPeriodic(.005);
        loadRightBuffer.startPeriodic(.005);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {

        Robot.getDrivetrain().getRightMaster().getMotionProfileStatus(rightStatus);
        Robot.getDrivetrain().getLeftMaster().getMotionProfileStatus(leftStatus);
        //System.out.println("Bottom buffer count: " + rightStatus.btmBufferCnt);
        //System.out.println("Top buffer count: " + rightStatus.topBufferCnt);


        if (rightStatus.isUnderrun || leftStatus.isUnderrun)
        {
            // if either MP has underrun, stop both
            System.out.println("Motion profile has underrun!");
            setValue = SetValueMotionProfile.Disable;
        } else if (rightStatus.btmBufferCnt > kMinPointsInTalon && leftStatus.btmBufferCnt > kMinPointsInTalon)
        {
            // if we have enough points in the talon, go.
            setValue = SetValueMotionProfile.Enable;
        } else if (rightStatus.activePointValid && rightStatus.isLast && leftStatus.activePointValid
                && leftStatus.isLast)
        {
            // if both profiles are at their last points, hold the last point
            setValue = SetValueMotionProfile.Hold;
        }

        Robot.getDrivetrain().getLeftMaster().set(ControlMode.MotionProfile, setValue.value);
        Robot.getDrivetrain().getRightMaster().set(ControlMode.MotionProfile, setValue.value);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        if (!hasPathStarted)
        {
            return false;
        }
        boolean leftComplete = leftStatus.activePointValid && leftStatus.isLast;
        boolean rightComplete = rightStatus.activePointValid && rightStatus.isLast;
        boolean trajectoryComplete = leftComplete && rightComplete;
        if (trajectoryComplete)
        {
            System.out.println("Finished trajectory");
        }
        return trajectoryComplete || isFinished;
    }

    // Called once after isFinished returns true
    protected void end()
    {
        loadLeftBuffer.stop();
        loadRightBuffer.stop();
        resetTalon(Robot.getDrivetrain().getRightMaster(), ControlMode.PercentOutput, 0);
        resetTalon(Robot.getDrivetrain().getLeftMaster(), ControlMode.PercentOutput, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
        loadLeftBuffer.stop();
        loadRightBuffer.stop();
        resetTalon(Robot.getDrivetrain().getRightMaster(), ControlMode.PercentOutput, 0);
        resetTalon(Robot.getDrivetrain().getLeftMaster(), ControlMode.PercentOutput, 0);
    }

    // set up the talon for motion profile control
    private void setUpTalon(TalonSRX talon)
    {
        talon.clearMotionProfileTrajectories();
        talon.changeMotionControlFramePeriod(5);
        talon.clearMotionProfileHasUnderrun(10);
    }

    // set the 	 to the desired controlMode
    // used at the end of the motion profile
    private void resetTalon(TalonSRX talon, ControlMode controlMode, double setValue)
    {
        talon.clearMotionProfileTrajectories();
        talon.clearMotionProfileHasUnderrun(10);
        talon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        talon.set(controlMode, setValue);
    }

    private class BufferLoader implements Runnable
    {
        private int lastPointSent = 0;
        private TalonSRX talon;
        private SrxMotionProfile prof;
        private int pidfSlot;
        private boolean reverse;

        public BufferLoader(TalonSRX talon, SrxMotionProfile prof, int pidfSlot, boolean reverse)
        {
            this.talon = talon;
            this.prof = prof;
            this.pidfSlot = pidfSlot;
            this.reverse = reverse;
        }

        public void run()
        {
            talon.processMotionProfileBuffer();

            if (lastPointSent >= prof.numPoints)
            {
                return;
            }

            while (!talon.isMotionProfileTopLevelBufferFull() && lastPointSent < prof.numPoints)
            {
                TrajectoryPoint point = new TrajectoryPoint();
                /* for each point, fill our structure and pass it to API */
                point.position = prof.points[lastPointSent][0] * (reverse ? -1 : 1);
                point.velocity = prof.points[lastPointSent][1] * (reverse ? -1 : 1);
                point.timeDur = TrajectoryDuration.Trajectory_Duration_10ms;
                point.profileSlotSelect0 = pidfSlot;
                point.profileSlotSelect1 = pidfSlot;
                point.zeroPos = false;
                if (lastPointSent == 0)
                {
                    point.zeroPos = true; /* set this to true on the first point */
                    System.out.println("Loaded first trajectory point");
                }

                point.isLastPoint = false;
                if ((lastPointSent + 1) == prof.numPoints)
                {
                    point.isLastPoint = true; /** set this to true on the last point */
                    System.out.println("Loaded last trajectory point");
                }

                talon.pushMotionProfileTrajectory(point);
                lastPointSent++;
                hasPathStarted = true;
            }
        }
    }
}