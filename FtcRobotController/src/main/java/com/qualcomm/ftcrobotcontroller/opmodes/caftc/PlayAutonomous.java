package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.content.Context;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will play what was recorded in RecordAutonomous. It loads a RecordedMoves class and plays it.
 */
public class PlayAutonomous extends BasicAutonomous{


    /*
    currenttimems + encoder = targettimems
    when currenttimems = targettimems

     */
    //TODO fix encoder reset system
    private final double DRIVESPEED = 1;

    long encoderCounter;

    String recording;
    String[] recordingLines;

    ArrayList<String> leftMotorLines;
    ArrayList<String> rightMotorLines;

    String filename;

    int currentLeftMotorLine;
    int currentRightMotorLine;

    int lastTickLeftMotorLine;
    int lastTickRightMotorLine;

    long lTargetTime;
    long rTargetTime;

    long lEncoderValue;
    long rEncoderValue;

    @Override
    public void init(){
        super.init();


        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        encoderCounter = 0;

        leftMotorLines = new ArrayList<String>();
        rightMotorLines = new ArrayList<String>();

        currentLeftMotorLine = 0;
        currentRightMotorLine = 0;
        lastTickLeftMotorLine = -1;
        lastTickRightMotorLine = -1;

        filename = "autonomousrecordingtest.txt";

        try {
            this.recording = ReadFile(filename);
        }catch(IOException e){
            telemetry.addData("Error", "IOException " + e);
        }

        recordingLines = recording.split(System.getProperty("line.separator"));

        for(int i = 0; i < recordingLines.length; i++){
            if(recordingLines[i].contains("leftMotor")){
                leftMotorLines.add(recordingLines[i]);
            }else if(recordingLines[i].contains("rightMotor")){
                rightMotorLines.add(recordingLines[i]);
            }
        }

    }
    @Override
    public void start() {
        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    @Override
    public void loop() {

        if(encoderCounter % 2 == 0){
            driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }


        super.loop();

            //left
            if (lastTickLeftMotorLine != currentLeftMotorLine) {
                lastTickLeftMotorLine = currentLeftMotorLine;
                lEncoderValue = Long.parseLong(leftMotorLines.get(currentLeftMotorLine).replaceAll("[^0-9]", ""));
                telemetry.addData("lEncoderValue", lEncoderValue);
                lTargetTime = System.currentTimeMillis() + lEncoderValue;

            }


            //right
            if (lastTickRightMotorLine != currentRightMotorLine) {
                lastTickRightMotorLine = currentRightMotorLine;

                rEncoderValue = Long.parseLong(rightMotorLines.get(currentRightMotorLine).replaceAll("[^0-9]", ""));
                rTargetTime = System.currentTimeMillis() + rEncoderValue;
            }



            //left
            if (leftMotorLines.get(currentLeftMotorLine).contains("INACTIVE")) {
                if (System.currentTimeMillis() >= lTargetTime) {
                    if (currentLeftMotorLine + 1 < leftMotorLines.size())
                        currentLeftMotorLine++;

                    if(encoderCounter % 2 != 0)
                        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
            } else {
                if (driveLeft.getCurrentPosition() < lEncoderValue) {

                    driveLeft.setPower(DRIVESPEED * (leftMotorLines.get(currentLeftMotorLine).contains("FORWARD") ? 1 : -1));
                } else {
                    if (currentLeftMotorLine + 1 < leftMotorLines.size())
                        currentLeftMotorLine++;
                    if(encoderCounter % 2 != 0)
                        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
            }

            telemetry.addData("left current pos", driveLeft.getCurrentPosition());

            //right
            if (rightMotorLines.get(currentRightMotorLine).contains("INACTIVE")) {
                if (System.currentTimeMillis() >= rTargetTime) {
                    if (currentRightMotorLine + 1 < rightMotorLines.size())
                        currentRightMotorLine++;
                }
            } else {
                if (driveRight.getCurrentPosition() < rEncoderValue) {
                    driveRight.setPower(DRIVESPEED * (rightMotorLines.get(currentRightMotorLine).contains("FORWARD") ? 1 : -1));
                } else {
                    if (currentRightMotorLine + 1 < rightMotorLines.size())
                        currentRightMotorLine++;
                    if(encoderCounter % 2 != 0)
                        driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
            }



    }
    @Override
    public void stop(){
        
    }
    private String ReadFile(String filename) throws IOException {

        String recording = "";

        File file = new File(FtcRobotControllerActivity.context.getFilesDir(), filename);

        int length = (int)file.length();

        byte[] bytes = new byte[length];

        FileInputStream inputStream = new FileInputStream(file);
        try{
            inputStream.read(bytes);
        }finally{
            inputStream.close();
        }
        return new String(bytes);
    }
}
