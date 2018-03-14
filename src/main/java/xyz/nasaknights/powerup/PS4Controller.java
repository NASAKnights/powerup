package xyz.nasaknights.powerup;

public class PS4Controller
{
    public enum Buttons
    {
        TRIANGLE(4),
        SQUARE(3),
        CIRCLE(2),
        X(1),
        RIGHT_BUMPER(6),
        LEFT_BUMPER(5),
        SHARE(7),
        OPTIONS(8),
        LEFT_JOYSTICK(9),
        RIGHT_JOYSTICK(10);

        private int id;

        Buttons(int id)
        {
            this.id = id;
        }

        /**
         * Returns the ID of the button selected with the enumerator.
         *
         * @return Button ID for PS4 Controller
         * @author Bradley Hooten
         * @since 2018.1.1-ALPHA1 (Commit ed4a93fe)
         */
        public int getID()
        {
            return this.id;
        }
    }

    public enum Axis
    {
        LEFT_X(0),
        LEFT_Y(1),
        LEFT_BUMPER(2),
        RIGHT_BUMPER(3),
        RIGHT_X(4),
        RIGHT_Y(5);

        private int id;

        Axis(int id)
        {
            this.id = id;
        }

        /**
         * Returns the ID of the axis selected with the enumerator.
         *
         * @return Axis ID for PS4 Controller
         * @author Bradley Hooten
         * @since 2018.1.1-ALPHA1
         */
        public int getID()
        {
            return this.id;
        }
    }
}
