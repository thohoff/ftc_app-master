package com.qualcomm.ftcrobotcontroller.opmodes.caftc;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will store the robot's movements, determined by a human controller, in a RecordedMoves object.
 */
public class RecordAutonomous extends BasicHardware{


    private String filename; //example "autonomous1.txt"
    private String recording;


    Motor leftMotor;
    Motor rightMotor;


    @Override
    public void init(){
        super.init();

        filename = "autonomousrecordingtest.txt";

        leftMotor = new Motor();
        rightMotor = new Motor();
    }
    @Override

    public void start(){

        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        recording = "";
    }
    @Override
    public void loop() {

        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        leftMotor.lastTickMotorState = leftMotor.motorState;
        rightMotor.lastTickMotorState = rightMotor.motorState;

        TestMotorInput();

        //TODO rightMotor doesn't output anything except for inactive for some reason

        /*The idea here is that there will always be a time for each motor, so it would look something like this:
          leftMotor : 1570 : INACTIVE
          rightMotor : 10532 : FORWARD
          leftMotor : 1054 : FORWARD
          leftMotor : 3952 : BACKWARD

          The reader will separate these statements into arrays of similar Motors (so a rightMotor array and a leftMotor array)
          and loop through them at the same time.
         */

        //TODO figure out how to implement wheel rotations instead of times.
        for(int i = 0; i < MotorState.values().length; i++){
            if(leftMotor.motorState != leftMotor.lastTickMotorState){
                recording = "leftMotor : " + driveLeft.getCurrentPosition() +  " : " + leftMotor.lastTickMotorState + "\n";

                driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            }
            if(rightMotor.motorState != rightMotor.lastTickMotorState){
                recording = "rightMotor : " + driveLeft.getCurrentPosition() + " : " + leftMotor.lastTickMotorState + "\n";

                driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

            }
        }

        telemetry.addData("recording", recording);

    }
    @Override
    public void stop(){
        try {
            WriteFile(filename, recording);
        }catch(IOException e){
            System.out.println("IOException thrown");
        }
    }
    private void WriteFile(String filename, String input) throws IOException{

        PrintWriter printWriter = new PrintWriter(filename);

        printWriter.println(input);
        printWriter.close();
    }
    private String ReadFile(String filename) throws IOException{

        String recording = "";

        File file = new File(filename);
        Scanner reader = new Scanner(file);

        while(reader.hasNext()){
            recording += reader.nextLine();
        }

        return recording;
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

        public MotorState MotorState(){
            return motorState;
        }
    }

    private enum MotorState {
        FORWARD,
        BACKWARD,
        INACTIVE;

    }

}
