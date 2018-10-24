package xyz.nasaknights.powerup.threading;

public abstract class RecurringThread extends Thread
{
    private boolean stopped = false;

    @Override
    public final void run()
    {
        while (!stopped)
        {
            action();
        }
    }

    public void peacefullyInterrupt()
    {
        stopped = true;
    }

    public abstract void action();
}
