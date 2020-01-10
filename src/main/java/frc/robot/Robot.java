package frc.robot;

//import ca.team3571.offseason.auto.AutonomousExecutor;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.FollowBall;
import frc.robot.commands.LiftManualCommand;
import frc.robot.commands.OpenCloseCommand;
import frc.robot.commands.auto.PracticeAuto;
//import ca.team3571.offseason.commands.auto.PracticeAuto;
import frc.robot.component.CameraController;
import frc.robot.component.ColorSensor;
import frc.robot.component.RobotCamera;
import frc.robot.component.Vision;
import frc.robot.subsystem.*;
import frc.robot.subsystem.elevator.Elevator;
import frc.robot.subsystem.elevator.LiftCommand;
import frc.robot.util.RioDuino;
import frc.robot.util.RobotMath;
import frc.robot.util.XboxController;
import edu.wpi.cscore.UsbCamera;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
//import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Robot extends TimedRobot //TimedRobot
{

    private DriveTrain driveTrain;
    private Elevator elevator;
    private Intake intake;
    private Tilt tilt;
    private Pneumatics pneumatics;
    private Logger logger;
    private RioDuino rioDuino;
    private CameraController cameraController;
    private ColorSensor colorSensor;
    private XboxController subsystemController = new XboxController(1);
    private PowerDistributionPanel pdp;
    private AHRS navx;
    private static Robot exposedInstance;
    private Vision visionProcessor;

    public static Robot getInstance() {
        if(exposedInstance==null) {
            throw new IllegalStateException("#robotInit must finish its invocation!");
        }
        return exposedInstance;
    }

    public Vision getVisionProcessor() {
		return visionProcessor;
	}

	public void setVisionProcessor(Vision visionProcessor) {
		this.visionProcessor = visionProcessor;
	}

	public XboxController getSubsystemController() {
        return subsystemController;
    }

    @Override
    public void robotInit() {
        //set reference for exposed instance
        exposedInstance = this;

        //power distribution
        pdp = new PowerDistributionPanel();

        //initialize subsystems
        pneumatics = new Pneumatics();
        driveTrain = new DriveTrain();
        elevator = new Elevator();
        intake = new Intake();
        tilt = new Tilt();

        //logger
        logger = Logger.getLogger(getClass().getName());

        //rio
        rioDuino = new RioDuino();

        navx = new AHRS(SPI.Port.kMXP); 

        setVisionProcessor(new Vision());

        colorSensor = new ColorSensor();
        runCamera();
        initController();
    }

    @Override
    public void autonomousInit() {
//        boolean signalReceived = false;
//        String signal = null;
//        while(!signalReceived) {
//            signal = DriverStation.getInstance().getGameSpecificMessage();
//            if(signal.length()==AutonomousExecutor.SIGNAL_LENGTH) {
//                signalReceived = true;
//            }
//        }
//        new AutonomousExecutor().accept(signal);
        new PracticeAuto().start();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run(); //dont delete this u idiot
        //teleopPeriodic();
        
        debug();
    }

    @Override
    public void teleopPeriodic() {
       // driveTrain.refresh();
        
        elevator.refresh();
        intake.refresh();
        tilt.refresh();
        colorSensor.matchedColor();
        subsystemController.refresh();
        visionProcessor.refresh();
        Scheduler.getInstance().run();

        if (visionProcessor.yellowBallxPos() > 0.05) driveTrain.tankdrive(0.7, -0.7);
        else if (visionProcessor.yellowBallxPos() < -0.05) driveTrain.tankdrive(-0.7, 0.7);
        else  driveTrain.arcadeDrive(0, 0);

        debug();
    }

    @Override
    public void testPeriodic() {
        new Thread() {
            @Override
            public void start() {
                while(true) {
                    System.out.println(rioDuino.receiveData());
                    for(byte b: rioDuino.getRaw()) {
                        System.out.print(" "+(int)b);
                    }
                    try {
                        sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void runCamera() {
        UsbCamera cameraOne = CameraServer.getInstance().startAutomaticCapture(0);
        UsbCamera cameraTwo = CameraServer.getInstance().startAutomaticCapture(1);
        //UsbCamera cameraThree = CameraServer.getInstance().startAutomaticCapture(2);
        cameraOne.setWhiteBalanceAuto();
        cameraTwo.setWhiteBalanceAuto();
        //cameraThree.setWhiteBalanceAuto();

        cameraController = new CameraController();//new RobotCamera(cameraOne), new RobotCamera(cameraTwo));
        cameraController.addCamera(new RobotCamera(cameraOne));
        cameraController.addCamera(new RobotCamera(cameraTwo));
        //cameraController.addCamera(new RobotCamera(cameraThree));
        cameraController.begin();
        cameraController.setPriority(0);
    }

    private void debug() {
        driveTrain.log();
        elevator.log();
        intake.log();
        tilt.log();
        visionProcessor.log();
        lognavx();
    }

    private void lognavx(){
        SmartDashboard.putNumber("DriveTrain/Position/Yaw", navx.getYaw());
    }

    public void log(Level logLevel, String message) {
        logger.log(logLevel, message);
    }

    //getters
    public DriveTrain getDrive() {
        return driveTrain;
    }

    public Pneumatics getPneumatics() {
        return pneumatics;
    }

    public Intake getIntake() {
        return intake;
    }

    public Tilt getTilt() {
        return tilt;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public PowerDistributionPanel getPowerData() {
        return pdp;
    }

    public AHRS getNAVX(){
        return navx;
    }

    private void initController() {
     //   subsystemController.Buttons.Y.runCommand(new ClimbCommand(), XboxController.CommandState.WhenPressed);
     //   subsystemController.Buttons.LB.runCommand(new LiftCommand(true), XboxController.CommandState.WhenPressed);
      //  subsystemController.Buttons.RB.runCommand(new LiftCommand(false), XboxController.CommandState.WhenPressed);

        //climbing  
        subsystemController.Buttons.Y.runCommand(new ClimbCommand(), XboxController.CommandState.WhenPressed);
      
        //intake  
        subsystemController.Buttons.A.runCommand(new OpenCloseCommand(), XboxController.CommandState.WhenPressed);
        subsystemController.Buttons.B.runCommand(new FollowBall(), XboxController.CommandState.WhenPressed);
        //subsystemController.Buttons.B.runCommand(new TiltCommand(), XboxController.CommandState.WhenPressed);
        
        //elevator 
        subsystemController.Buttons.LB.runCommand(new LiftCommand(true), XboxController.CommandState.WhenPressed);
        subsystemController.Buttons.RB.runCommand(new LiftCommand(false), XboxController.CommandState.WhenPressed);
    }

}
