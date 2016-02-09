package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will play what was recorded in RecordAutonomous. It loads a RecordedMoves class and plays it.
 */
public class PlayAutonomous extends BasicAutonomous{
    @Override
    public void init(){
        super.init();
    }
    @Override
    public void start() {

    }
    @Override
    public void loop() {

    }
    @Override
    public void stop(){

    }




    //Code they provided to use encoders
    //Use drive_using_encoders to drive


    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (driveLeft != null)
        {
            l_return = driveLeft.getPower ();
        }

        return l_return;

    } // a_left_drive_power
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (driveRight != null)
        {
            l_return = driveRight.getPower ();
        }

        return l_return;

    }

    void set_drive_power (double p_left_power, double p_right_power)

    {
        if (driveLeft != null)
        {
            driveLeft.setPower (p_left_power);
        }
        if (driveRight != null)
        {
            driveRight.setPower (p_right_power);
        }

    }
    boolean has_left_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (driveLeft != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (driveLeft.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reached
    //
    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (driveRight != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (driveRight.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reached
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean have_drive_encoders_reached
    ( double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_drive_encoder_reached (p_left_count) &&
                has_right_drive_encoder_reached (p_right_count))
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_encoders_reached

    //--------------------------------------------------------------------------
    //
    // drive_using_encoders
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean drive_using_encoders
    ( double p_left_power
            , double p_right_power
            , double p_left_count
            , double p_right_count
    )

    {
        boolean l_return = false;

        run_using_encoders ();
        set_drive_power (p_left_power, -p_right_power);
        if (have_drive_encoders_reached (p_left_count, p_right_count))
        {
            reset_drive_encoders ();
            set_drive_power (0.0f, 0.0f);
            l_return = true;
        }
        return l_return;

    }
    boolean has_left_drive_encoder_reset ()
    {
        boolean l_return = false;

        if (a_left_encoder_count() == 0)
        {
            l_return = true;
        }

        return l_return;
    }
    boolean has_right_drive_encoder_reset ()
    {
        boolean l_return = false;
        if (a_right_encoder_count() == 0)
        {
            l_return = true;
        }
        return l_return;

    }
    boolean have_drive_encoders_reset ()
    {
        boolean l_return = false;
        if (has_left_drive_encoder_reset () && has_right_drive_encoder_reset ())
        {
            l_return = true;
        }
        return l_return;

    }
    int a_left_encoder_count ()
    {
        int l_return = 0;

        if (driveLeft != null)
        {
            l_return = driveLeft.getCurrentPosition ();
        }

        return l_return;

    }
    int a_right_encoder_count ()

    {
        int l_return = 0;

        if (driveRight != null)
        {
            l_return = driveRight.getCurrentPosition ();
        }

        return l_return;

    }
    public void run_using_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_using_left_drive_encoder ();
        run_using_right_drive_encoder ();

    }
    public void run_using_left_drive_encoder ()

    {
        if (driveLeft != null)
        {
            driveLeft.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    }
    public void run_using_right_drive_encoder ()

    {
        if (driveRight != null)
        {
            driveRight.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    }
    public void reset_left_drive_encoder ()

    {
        if (driveLeft != null)
        {
            driveLeft.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }
    public void reset_right_drive_encoder ()

    {
        if (driveRight != null)
        {
            driveRight.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }
    public void reset_drive_encoders ()

    {
        reset_left_drive_encoder ();
        reset_right_drive_encoder ();

    }
    public void run_without_left_drive_encoder ()

    {
        if (driveLeft != null)
        {
            if (driveLeft.getMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                driveLeft.setMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }
    public void run_without_right_drive_encoder ()

    {
        if (driveRight != null)
        {
            if (driveRight.getMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                driveRight.setMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }
    public void run_without_drive_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_without_left_drive_encoder ();
        run_without_right_drive_encoder ();

    }



}
