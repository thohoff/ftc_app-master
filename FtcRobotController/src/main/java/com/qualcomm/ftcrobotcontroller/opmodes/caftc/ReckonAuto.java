package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.graphics.Color;
import android.os.Build;

import com.badlogic.gdx.math.Vector2;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Thomas_Hoffmann on 1/9/2016.
 */
enum AutoMode {FIRST_MOVE, PREPARE_TO_MOVE, MOVE_OUT, ALIGN_TO_BEACON,MOVE_TO_BEACON, LOCK_TO_BEACON, DROP_PAYLOAD, PARK, STOP}

public class ReckonAuto extends BasicAutonomous{
    AutoMode mode = AutoMode.FIRST_MOVE;
    ColorSensor colorSensor;
    UltrasonicSensor sonic;
    CompassSensor compass;
    private static float u = 4;
    public static final Vector2 start1 = new Vector2(60,18);
    public static final Vector2 start2 = new Vector2(84,18);
    public static final Vector2 waypoint1 = new Vector2(72, 48);
    public static final Vector2 beaconloc = new Vector2(12, 84);
    private Vector2 position = start1;
    public static final double standardPower = 0.75f;
    public static final double feetPerRotation = 1;
    public static final double unitPerRotation = 12*feetPerRotation;
    private int sweepDir = 1; // 1 means Right, -1 means last direction was left.
    private Vector2 encoderStartState;
    private double distanceMoved;
    private double initialRotation;
    private int lockCount = 0;
    private int maxLockCount = 100;
    private boolean isBlue = FtcRobotControllerActivity.colorSwitch.isChecked();
    @Override
    public void init(){
        super.init();
        colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(true);
        sonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        compass = hardwareMap.compassSensor.get("compass");
        dLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        initialRotation = compass.getDirection();
        reset_drive_encoders();
        run_without_drive_encoders();
    }
    @Override
    public void start(){
        reset_drive_encoders();
    }
    @Override
    public void loop(){
        switch (mode){
            case FIRST_MOVE:

                if(SmartMove(6,standardPower)){
                    stopMoving();
                    reset_drive_encoders();
                    run_without_drive_encoders();
                    mode = AutoMode.PREPARE_TO_MOVE;
                }
                break;
            case PREPARE_TO_MOVE:
                run_without_drive_encoders();
                if(SmartRotate(getDesiredRotation(waypoint1),standardPower)){
                    stopMoving();
                    reset_drive_encoders();
                    run_using_encoders();

                    mode = AutoMode.MOVE_OUT;
                }
                break;
            case MOVE_OUT :

                if(SmartMove(position.dst(waypoint1),standardPower)){
                    stopMoving();
                    position = waypoint1.cpy();
                    reset_drive_encoders();
                    run_without_drive_encoders();
                    mode = AutoMode.ALIGN_TO_BEACON;
                }
                break;
            case ALIGN_TO_BEACON:
                run_without_drive_encoders();
                if(SmartRotate(getDesiredRotation(beaconloc),standardPower)) {
                    stopMoving();
                    mode = AutoMode.MOVE_TO_BEACON;

                }
                break;
            case MOVE_TO_BEACON:
                run_without_drive_encoders();
                if (isWhite()) {
                    stopMoving();
                    mode = AutoMode.LOCK_TO_BEACON;
                }else{
                    moveForward(standardPower);
                }
                break;
            case LOCK_TO_BEACON:
                run_without_drive_encoders();
                if(sonic.getUltrasonicLevel()<4){
                    stopMoving();
                    mode  = AutoMode.DROP_PAYLOAD;
                }
                else if (isWhite()){
                    moveForward(standardPower);
                    lockCount = 0;
                }
                else{
                    lockCount++;
                    if(lockCount >= maxLockCount){
                        sweepDir *= -1;
                        lockCount = 0;
                        maxLockCount += 50;
                    }
                    turnRight(standardPower*-1*sweepDir);
                }
                break;
            case DROP_PAYLOAD:
                break;
            case PARK:
                break;
            case STOP:
                stopMoving();
                break;
            default:
                stopMoving();
                break;

        }
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("position", position);
        telemetry.addData("rotation", compass.getDirection());
        telemetry.addData("initial rotation", initialRotation);
        telemetry.addData("sonic", sonic.getUltrasonicLevel());
        telemetry.addData("isBlue", isBlue);
        telemetry.addData("State", mode);
        telemetry.addData("Motor State", dRight.getMode()+", "+dLeft.getMode());

    }
    public void moveForward(double amount){
        dRight.setPower(-amount);
        dLeft.setPower(amount);
    }
    public void moveBackward(double amount) {
        moveForward(-amount);
    }
    public void stopMoving(){
        set_drive_power(0, 0);
    }
    public void turnLeft(double amount){
        turnRight(-amount);
    }
    public void turnRight(double amount){
        dRight.setPower(amount);
        dLeft.setPower(amount);
    }
    public int turnSign(double start, double target){
        double result = 2*Math.floor(Math.sin(Math.toRadians(target-start)))+1;
        if(result>0){
            return 1;
        }
        return -1;
    }
    public boolean SmartRotate(double target, double power){
        double dir = compass.getDirection();
        if(Math.abs(dir-target)<10){
            return true;
        }
        telemetry.addData("dir-target", Math.abs(dir-target)+" ,"+turnSign(dir,target)+", target"+target);
        telemetry.addData("turn power",turnSign(dir, target) * power);
        turnRight(turnSign(dir, target) * power);
        return false;
    }
    public boolean SmartMove(double distance, double power){
       return drive_using_encoders(power, power, distance*360,distance*360);
    }
    public void prepareSmartMove(){
        encoderStartState = new Vector2(dLeft.getCurrentPosition(), dRight.getCurrentPosition());
    }
    public boolean isWhite(){
        return true;
    }
    public float getDesiredRotation(Vector2 target){
        Vector2 subTarget = target.cpy().sub(position);
        return (subTarget.angle(position)+180)%360;
    }
    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (dLeft != null)
        {
            l_return = dLeft.getPower ();
        }

        return l_return;

    } // a_left_drive_power
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (dRight != null)
        {
            l_return = dRight.getPower ();
        }

        return l_return;

    }
    
    void set_drive_power (double p_left_power, double p_right_power)

    {
        if (dLeft != null)
        {
            dLeft.setPower (p_left_power);
        }
        if (dRight != null)
        {
            dRight.setPower (p_right_power);
        }

    }
    boolean has_left_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (dLeft != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (dLeft.getCurrentPosition ()) > p_count)
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

        if (dRight != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (dRight.getCurrentPosition ()) > p_count)
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
        telemetry.addData("Encoder Reacher", l_return);
        telemetry.addData("Right Encoder", dRight.getCurrentPosition());
        telemetry.addData("LeftEncoder", dLeft.getCurrentPosition());
        telemetry.addData("count", p_left_count+ "Right: "+ p_right_count);
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

        if (dLeft != null)
        {
            l_return = dLeft.getCurrentPosition ();
        }

        return l_return;

    } 
    int a_right_encoder_count ()

    {
        int l_return = 0;

        if (dRight != null)
        {
            l_return = dRight.getCurrentPosition ();
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
        if (dLeft != null)
        {
            dLeft.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } 
    public void run_using_right_drive_encoder ()

    {
        if (dRight != null)
        {
            dRight.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    }
    public void reset_left_drive_encoder ()

    {
        if (dLeft != null)
        {
            dLeft.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } 
    public void reset_right_drive_encoder ()

    {
        if (dRight != null)
        {
            dRight.setMode
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
        if (dLeft!= null)
        {
            if (dLeft.getMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                dLeft.setMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }
    public void run_without_right_drive_encoder ()

    {
        if (dRight!= null)
        {
            if (dRight.getMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                dRight.setMode
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
