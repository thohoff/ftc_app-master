package com.qualcomm.ftcrobotcontroller.opmodes.caftc;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will store the robot's movements, determined by a human controller, in a RecordedMoves object.
 */
public class RecordAutonomous extends BasicAutonomous{
    

    private String filename; //example "autonomous1.txt"

    @Override
    public void init(){
        super.init();
    }
    @Override

    public void start(){

    }
    @Override
    public void loop() {

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
}
