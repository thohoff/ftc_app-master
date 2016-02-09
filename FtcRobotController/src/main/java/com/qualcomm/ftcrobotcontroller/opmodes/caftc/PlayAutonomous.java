package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will play what was recorded in RecordAutonomous. It loads a RecordedMoves class and plays it.
 */
public class PlayAutonomous extends BasicAutonomous{

    String recording;
    String[] recordingLines;

    ArrayList<String> leftMotorLines;
    ArrayList<String> rightMotorLines;

    String filename;

    int currentLeftMotorLine;
    int currentRightMotorLine;

    int lastTickLeftMotorLine;
    int lastTickRightMotorLine;

    int lEncoderValue;
    int rEncoderValue;

    @Override
    public void init(){
        super.init();

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

    }
    @Override
    public void loop() {

        if(lastTickLeftMotorLine != currentLeftMotorLine){
            lastTickLeftMotorLine = currentLeftMotorLine;

            String leftEncoderValue = leftMotorLines.get(currentLeftMotorLine);
            leftEncoderValue.replaceAll("[^0-9]", "");
            lEncoderValue = Integer.parseInt(leftEncoderValue);
        }
        if(lastTickRightMotorLine != currentRightMotorLine){
            lastTickRightMotorLine = currentRightMotorLine;

            String rightEncoderValue = rightMotorLines.get(currentRightMotorLine);
            rightEncoderValue.replaceAll("[^0-9]","");
            rEncoderValue = Integer.parseInt(rightEncoderValue);
        }

        if(lEncoderValue > Math.abs(driveLeft.getCurrentPosition())){

        }

    }
    @Override
    public void stop(){

    }
    private String ReadFile(String filename) throws IOException {

        String recording = "";

        File file = new File(filename);
        Scanner reader = new Scanner(file);

        while(reader.hasNext()){
            recording += reader.nextLine();
        }

        return recording;
    }
}
