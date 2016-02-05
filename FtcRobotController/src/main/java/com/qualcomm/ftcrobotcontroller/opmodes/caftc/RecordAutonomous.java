package com.qualcomm.ftcrobotcontroller.opmodes.caftc;


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

    float leftMotorTime;  //Time in ms
    float rightMotorTime;  //Time in ms

    @Override
    public void init(){
        super.init();

        filename = "autonomousrecordingtest.txt";

        leftMotor = new Motor();
        rightMotor = new Motor();



        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    @Override

    public void start(){
        leftMotorTime = 0;
        rightMotorTime = 0;

        recording = "";
    }
    @Override
    public void loop() {

        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);


        leftMotorTime++;
        rightMotorTime++;

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
                recording += "leftMotor : " + leftMotorTime +  " : " + leftMotor.lastTickMotorState + "\n";
                leftMotorTime = 0;
            }
            if(rightMotor.motorState != rightMotor.lastTickMotorState){
                recording += "rightMotor : " + rightMotorTime + " : " + leftMotor.lastTickMotorState + "\n";
                rightMotorTime = 0;
            }
        }

    }
    @Override
    public void stop(){

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
        FORWARD (0f),
        BACKWARD (0f),
        INACTIVE (0f);

        private float time; //Time in milliseconds

        private MotorState(float time){
            this.time = time;
        }

        public void AddTime(float time){
            this.time += time;
        }

        public float GetTime(){
            return time;
        }

    }

}
