# ftc_app

A program for FTC

This will be the program for the CA FTC 2016 Robot.

To develop the program it is recommended to have the following software: Android Studio : Required to program, it sets up everything you need, except maybe java, and the JDK. https://developer.android.com/sdk/index.html

JDK: Needed to compile java code (probably) http://www.oracle.com/technetwork/java/javase/downloads/index.html

Git for Windows : Easy way to connect to github. Allows for the Android Studio/github integration. **** IF you are installing Git for windows remember to set it to the path in the installation options or else android studio cannot find it. **** https://github.com/git-for-windows/git/releases/tag/v2.5.3.windows.1 (scroll down) FTC Software: Needed to use the motors and other robotics features of the robot.

TODO:

Get some basic code running a motor or something.

Get joystick control.









FTC stuff :


FTC Android Studio project to create FTC Robot Controller app.

This is the FTC SDK that can be used to create an FTC Robot Controller app, with custom op modes.
The FTC Robot Controller app is designed to work in conjunction with the FTC Driver Station app.
The FTC Driver Station app is available through Google Play.

To use this SDK, download/clone the entire project to your local computer.
Use Android Studio to import the folder  ("Import project (Eclipse ADT, Gradle, etc.)").

Documentation for the FTC SDK are included with this repository.  There is a subfolder called "doc" which contains several subfolders:

 * The folder "apk" contains the .apk files for the FTC Driver Station and FTC Robot Controller apps.
 * The folder "javadoc" contains the JavaDoc user documentation for the FTC SDK.
 * The folder "tutorial" contains PDF files that help teach the basics of using the FTC SDK.

For technical questions regarding the SDK, please visit the FTC Technology forum:

  http://ftcforum.usfirst.org/forumdisplay.php?156-FTC-Technology

In this latest version of the FTC SDK (20150803_001) the following changes should be noted:

 * New user interfaces for FTC Driver Station and FTC Robot Controller apps.
 * An init() method is added to the OpMode class.
   - For this release, init() is triggered right before the start() method.
   - Eventually, the init() method will be triggered when the user presses an "INIT" button on driver station.
   - The init() and loop() methods are now required (i.e., need to be overridden in the user's op mode).
   - The start() and stop() methods are optional.
 * A new LinearOpMode class is introduced.
   - Teams can use the LinearOpMode mode to create a linear (not event driven) program model.
   - Teams can use blocking statements like Thread.sleep() within a linear op mode.
 * The API for the Legacy Module and Core Device Interface Module have been updated.
   - Support for encoders with the Legacy Module is now working.
 * The hardware loop has been updated for better performance.


T. Eng
August 3, 2015

