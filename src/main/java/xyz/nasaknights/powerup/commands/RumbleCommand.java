package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class RumbleCommand extends Command {
    private Joystick toRumble;
    private boolean rumble;
    private boolean finished = false;

    public RumbleCommand(Joystick toRumble, boolean rumble)
    {
        this.toRumble = toRumble;
        this.rumble = rumble;
    }

    @Override
    public synchronized void start() {
        toRumble.setRumble(GenericHID.RumbleType.kLeftRumble, rumble ? 1 : 0);
        toRumble.setRumble(GenericHID.RumbleType.kRightRumble, rumble ? 1 : 0);
        finished = true;
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }
}
