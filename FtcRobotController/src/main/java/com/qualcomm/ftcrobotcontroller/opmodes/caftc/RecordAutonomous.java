package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.content.Context;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will store the robot's movements, determined by a human controller, in a RecordedMoves object.
 */
public class RecordAutonomous extends BasicHardware {




    private String filename; //example "autonomous1.txt"
    private String recording;


    Motor leftMotor;
    Motor rightMotor;

    float lInactiveTime;
    float rInactiveTime;


    @Override
    public void init() {


        super.init();

        filename = "autonomousrecordingtest.txt";

        leftMotor = new Motor();
        rightMotor = new Motor();

        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        recording = "";
    }

    @Override

    public void start() {

    }

    @Override
    public void loop() {
        super.loop();
        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        leftMotor.lastTickMotorState = leftMotor.motorState;
        rightMotor.lastTickMotorState = rightMotor.motorState;

        TestMotorInput();
        

        /*The idea here is that there will always be a time for each motor, so it would look something like this:
          leftMotor 1570 INACTIVE
          rightMotor 10532 FORWARD
          leftMotor 1054 FORWARD
          leftMotor 3952 BACKWARD

          The reader will separate these statements into arrays of similar Motors (so a rightMotor array and a leftMotor array)
          and loop through them at the same time.
         */


        for (int i = 0; i < MotorState.values().length; i++) {
            if (leftMotor.motorState != leftMotor.lastTickMotorState) {

                if(leftMotor.motorState == MotorState.INACTIVE){
                    lInactiveTime = System.nanoTime();
                }

                if (leftMotor.lastTickMotorState == MotorState.INACTIVE) {
                    recording += "leftMotor " + Math.abs(System.nanoTime() - lInactiveTime) + " " + leftMotor.lastTickMotorState + "\n";
                } else {

                    recording += "leftMotor " + Math.abs(driveLeft.getCurrentPosition()) + " " + leftMotor.lastTickMotorState + "\n";

                    driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

                }
            }
            if (rightMotor.motorState != rightMotor.lastTickMotorState) {
                if(rightMotor.motorState != rightMotor.lastTickMotorState){
                    rInactiveTime = System.nanoTime();
                }
                if (rightMotor.lastTickMotorState == MotorState.INACTIVE) {
                    recording += "rightMotor " + Math.abs(System.nanoTime() - rInactiveTime) + " " + rightMotor.lastTickMotorState + "\n";
                } else{
                    recording += "rightMotor " + Math.abs(driveRight.getCurrentPosition()) + " " + rightMotor.lastTickMotorState + "\n";
            }
            driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }


        telemetry.addData("recording", recording + "\n");

    }
    @Override
    public void stop(){
        try {
            WriteFile(filename, recording);
        }catch(IOException e){
            telemetry.addData("Error", "IOException thrown : " + e);
        }
    }
    private void WriteFile(String filename, String input) throws IOException{

        FileOutputStream outputStream;


        File file = new File(FtcRobotControllerActivity.context.getFilesDir(), filename);
        telemetry.addData("directory", file.getAbsolutePath());

        try{
            outputStream = FtcRobotControllerActivity.context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(input.getBytes());
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //Set motor state based on current power of the motor.
    private void TestMotorInput(){
        //Left Motor
        // TODO test to make sure that these values line up with what the robot actually does.
        if(driveLeft.getPower() > 0){
            leftMotor.motorState = MotorState.FORWARD;
        }
        else if(driveLeft.getPower() < 0){
            leftMotor.motorState = MotorState.BACKWARD;
        }else{
            leftMotor.motorState = MotorState.INACTIVE;
        }
        //Right Motor
        if(driveRight.getPower() > 0){
            rightMotor.motorState = MotorState.FORWARD;
        }
        else if(driveRight.getPower() < 0){
            rightMotor.motorState = MotorState.BACKWARD;
        }
        else{
            rightMotor.motorState = MotorState.INACTIVE;
        }
    }

    private class Motor {

        MotorState motorState;
        MotorState lastTickMotorState;

        public Motor(){
            motorState = MotorState.INACTIVE;
        }
    }

    private enum MotorState {
        FORWARD,
        BACKWARD,
        INACTIVE

    }

}
