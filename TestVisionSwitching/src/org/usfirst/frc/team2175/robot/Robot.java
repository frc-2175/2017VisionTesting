package org.usfirst.frc.team2175.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser<String> chooser = new SendableChooser<>();
    public static final String NETWORK_TABLE_NAME = "selectedCamera";
    public static final String CAMERA_SELECTION_NUMBER_NAME = "cameraNumber";

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        SmartDashboard.putNumber("cameraNumber",
                NetworkTable.getTable(NETWORK_TABLE_NAME)
                        .getNumber(CAMERA_SELECTION_NUMBER_NAME, 0));
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        new Thread(() -> {
            UsbCamera camera1 =
                    CameraServer.getInstance().startAutomaticCapture(0);
            UsbCamera camera2 =
                    CameraServer.getInstance().startAutomaticCapture(1);
            NetworkTable selectedCamera =
                    NetworkTable.getTable("selectedCamera");

            camera1.setResolution(640, 480);
            camera2.setResolution(640, 480);

            CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
            CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);

            CvSource outputStream =
                    CameraServer.getInstance().putVideo("Blur", 640, 480);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                double cameraNumber =
                        selectedCamera.getNumber("cameraNumber", 0);
                if (cameraNumber == 0) {
                    cvSink1.grabFrame(source);
                } else {
                    cvSink2.grabFrame(source);
                }
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        autoSelected = chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        switch (autoSelected) {
        case customAuto:
            // Put custom auto code here
            break;
        case defaultAuto:
        default:
            // Put default auto code here
            break;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    }
}
