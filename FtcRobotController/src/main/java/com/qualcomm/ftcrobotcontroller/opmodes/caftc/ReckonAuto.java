package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Thomas_Hoffmann on 1/9/2016.
 
*/
enum AutoMode {FIRST_MOVE, ALIGN_TO_BEACON,MOVE_TO_BEACON,CHECK1, LOCK_TO_BEACON,CHECK2, APROACH_BEACON, DROP_PAYLOAD, POST_DROP, ALIGN_TO_PARK, PARK, ALIGN_TO_MOUNTAIN, GO_TO_MOUNTAIN, STOP}

public class ReckonAuto extends BasicAutonomous{
    AutoMode mode = AutoMode.FIRST_MOVE;
    public static Vector2 start = new Vector2(84,12);
    public static final Vector2 beaconloc = new Vector2(12, 84);
    public  static final  Vector2 park1 = new Vector2(60, 30);
    public  static  final Vector2 park2 = new Vector2(36, 30);
    public static  final Vector2 mountain = new Vector2(24, 36);
    public static boolean goingToPark = false;
    public static boolean isTarget2 = false;
    private Vector2 position = start;
    public static double standardPower = 0.7; //Double.parseDouble(FtcRobotControllerActivity.power.getEditableText().toString());
    public static final double inchesPerRotation = 2.3;
    private int sweepDir = 1; // 1 means Right, -1 means last direction was left.
    private Vector2 encoderStartState;
    private double distanceMoved;
    private double initialRotation;
    private int lockCount = 0;
    private int maxLockCount = 100;
    private boolean isRed;
    private boolean working = true;
    private Vector2 parkLoc;
    private double rotateDivisor = 70;
    @Override
    public void init(){
        super.init();
        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        initialRotation = 0;//compass.getDirection();
        reset_drive_encoders();
        run_without_drive_encoders();
        if(isTarget2){
            parkLoc = park2.cpy();
        }
        else  {parkLoc = park1.cpy();}
        //Get values from UI
       isRed = FtcRobotControllerActivity.colorSwitch.isChecked();
        goingToPark = false;//FtcRobotControllerActivity.parkActiveSwitch.isChecked();
        isTarget2 = false;//FtcRobotControllerActivity.park2Switch.isChecked();
        rotateDivisor = Double.parseDouble(FtcRobotControllerActivity.turnFactor.getEditableText().toString());
        start.x =  72;//(float) Double.parseDouble(FtcRobotControllerActivity.xLocIn.getEditableText().toString());
        standardPower = 0.5;//Double.parseDouble(FtcRobotControllerActivity.power.getEditableText().toString());

    }
    @Override
    public void start(){
        reset_drive_encoders();

    }
    @Override
    public void loop(){
        super.loop();
        switch (mode){
            case FIRST_MOVE:
                    if (SmartMove(6, standardPower)) {
                        stopMoving();
                        reset_drive_encoders();
                        mode = AutoMode.ALIGN_TO_BEACON;
                    }
                break;
            case ALIGN_TO_BEACON:
                    if (SmartRotate(getDesiredRotation(beaconloc), standardPower)) {
                        stopMoving();
                        reset_drive_encoders();
                        initialRotation += getDesiredRotation(beaconloc);
                        mode = AutoMode.MOVE_TO_BEACON;
                    }
                break;
            case MOVE_TO_BEACON:
                    if (SmartMove(position.dst(beaconloc)+12, standardPower)) {
                        stopMoving();
                        reset_drive_encoders();
                        position = beaconloc.cpy();
                        mode = AutoMode.CHECK1;
                    }
                break;
            case CHECK1:
                if(have_drive_encoders_reset()){
                    mode = AutoMode.LOCK_TO_BEACON;
                }
                else{
                    reset_drive_encoders();
                }
                break;
            case LOCK_TO_BEACON:
                    double angle = 90;
                    if (isRed) {
                        angle = -90;
                    }
                    double sign =1;
                    if (isRed) {
                        sign = -1;
                    }
                    if (drive_using_encoders(-sign*standardPower,sign*standardPower, 2900,2900)) {
                        stopMoving();
                        reset_drive_encoders();
                        initialRotation += angle;
                        mode = AutoMode.CHECK2;
                    }
                break;
            case CHECK2:
                if(have_drive_encoders_reset()){
                    mode = AutoMode.APROACH_BEACON;
                }
                else{
                    reset_drive_encoders();
                }
                break;
            case APROACH_BEACON:
                    if (SmartMove(24, standardPower)) {
                        stopMoving();
                        this.position = new Vector2(0, 84);
                        reset_drive_encoders();
                        mode = AutoMode.DROP_PAYLOAD;
                    }
                break;
            case DROP_PAYLOAD:
                reset_drive_encoders();
                climbersArm.setPosition(1);
                resetServos();
                position = new Vector2(0,84);
                mode = AutoMode.POST_DROP;
                break;
            case POST_DROP:
                climbersArm.setPosition(1);
                resetServos();
                reset_drive_encoders();
                    if(goingToPark) {
                    mode = AutoMode.ALIGN_TO_PARK;
                }
                else{
                    stopMoving();
                }
                break;
            case ALIGN_TO_PARK:
                if(SmartRotate(getDesiredRotation(parkLoc), standardPower)){
                    reset_drive_encoders();
                    initialRotation +=getDesiredRotation(parkLoc);
                  mode = AutoMode.PARK;
                }
                break;
            case PARK:
                if(SmartMove(position.dst(parkLoc),standardPower)){
                    reset_drive_encoders();
                    position = parkLoc.cpy();
                    if(isTarget2){
                        mode = AutoMode.ALIGN_TO_MOUNTAIN;
                    }else{
                        mode = AutoMode.STOP;
                    }
                }
                break;
                    case ALIGN_TO_MOUNTAIN:
                        if(SmartRotate(getDesiredRotation(mountain),standardPower)){
                            reset_drive_encoders();
                            mode = AutoMode.GO_TO_MOUNTAIN;
                }
                break;
                    case GO_TO_MOUNTAIN:
                moveForward(standardPower);
                        break;
            case STOP:
                stopMoving();
                break;
            default:
                stopMoving();
                break;

        }
     //   telemetry.addData("Power", Double.parseDouble(FtcRobotControllerActivity.power.getEditableText().toString()));
      //  telemetry.addData("ODS ", a_ods_light_detected());
        telemetry.addData("position", position);
        telemetry.addData("initial rotation", initialRotation);
     //   telemetry.addData("sonic", sonic.getUltrasonicLevel());
        telemetry.addData("isBlue", isRed);
        telemetry.addData("State", mode);
        telemetry.addData("Motor State", driveRight.getMode() + ", " + driveLeft.getMode());
        telemetry.addData("Desired", getDesiredRotation(beaconloc));
        telemetry.addData("Encoders", driveLeft.getCurrentPosition() +", "+driveRight.getCurrentPosition());
    }
    public void moveForward(double amount){
        driveRight.setPower(amount);
        driveLeft.setPower(-amount);
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
        driveRight.setPower(amount);
        driveLeft.setPower(amount);
    }
    public int turnSign(double start, double target){
        double result = -2*Math.floor(Math.sin(Math.toRadians(target-start)))+1;
        if(result>0){
            return 1;//-1
        }
        return 1;
    }
    public boolean SmartRotate(double degrees, double power){
        int sign = 1;
        if(degrees < 0){
            sign = -1;
        }
        //35, 17.5, etc ;
        return SmartMoveBoth(-1*degrees * 2.3 / rotateDivisor, degrees * 2.3 / rotateDivisor, -1*sign * power, sign*power);
    }
    public boolean SmartMove(double distance, double power) {
        return drive_using_encoders(power, power, (distance*360d)/inchesPerRotation,(distance*360d)/inchesPerRotation);
    }
    public boolean SmartMoveBoth(double distancel, double distancer, double powerl, double powerr){
        return drive_using_encoders(powerl,powerr,(distancel*360d)/inchesPerRotation,(distancer*360d)/inchesPerRotation);
    }
    public void prepareSmartMove(){
        encoderStartState = new Vector2(driveLeft.getCurrentPosition(), driveRight.getCurrentPosition());
    }
 /*   public boolean isWhite(){
        return a_ods_white_tape_detected();
    }*/
    public double getDesiredRotation(Vector2 target){
        Vector2 rotpos = position.cpy().rotate((float)Math.toRadians(initialRotation));
        Vector2 sub = target.cpy().sub(rotpos);
        double angle = ((Math.toDegrees(Math.atan2(sub.y,sub.x ))));
        if (isRed){
            angle = angle * -1;
        }
     //   if(angle < 0){
     //       angle+= 360;
     //   }
        return angle;
    }
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
            if (Math.abs (driveLeft.getCurrentPosition ()) > Math.abs(p_count))
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
            if (Math.abs (driveRight.getCurrentPosition ()) > Math.abs(p_count))
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
        set_drive_power (-p_left_power, p_right_power);
        if(has_left_drive_encoder_reached(p_left_count)){
            driveLeft.setPower(0);
        }
        if(has_right_drive_encoder_reached(p_right_count)){
            driveLeft.setPower(0);
        }
        if (have_drive_encoders_reached (p_left_count, p_right_count))
        {
            reset_drive_encoders ();
            set_drive_power (0.0f, 0.0f);
            l_return = true;
        }
        telemetry.addData("Encoder Reacher", l_return);
        telemetry.addData("Right Encoder", driveRight.getCurrentPosition());
        telemetry.addData("LeftEncoder", driveLeft.getCurrentPosition());
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
   /* double a_ods_light_detected ()

    {
         return optical.getLightDetected();

    } // a_ods_light_detected

    //--------------------------------------------------------------------------
    //
    // a_ods_white_tape_detected
    //
    /**
     * Access whether the EOP is detecting white tape.
     */
  /*  boolean a_ods_white_tape_detected ()

    {
        //
        // Assume not.
        //
        boolean l_return = false;

        if (optical != null)
        {
            //
            // Is the amount of light detected above the threshold for white
            // tape?
            //
            if (optical.getLightDetected () > 0.025)
            {
                l_return = true;
            }
        }

        //
        // Return
        //
        return l_return;

    }
    */

    public void resetServos()
    {
        armServo.setPosition(0.8);
        //climbersArm.setPosition(0);
        zipLeft.setPosition(0.5);
        zipRight.setPosition(0.5);
        rightArm.setPosition(0.2);
        leftArm.setPosition(0.9);
    }
}
