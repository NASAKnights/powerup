package xyz.nasaknights.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;

public class DelayCommand extends Command {
    private long timeout;
    private long startMillis = Long.MAX_VALUE;

    public DelayCommand(long timeout) {
        this.timeout = timeout;
    }

    @Override
    protected void initialize() {
        startMillis = System.currentTimeMillis();
    }

    @Override
    protected boolean isFinished() {
        return System.currentTimeMillis() > (startMillis + timeout);
    }
}
