package frc.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.FollowBall;
import frc.robot.commands.OpenCloseCommand;
import frc.robot.commands.auto.PracticeAuto;
import frc.robot.component.ColorSensor;
import frc.robot.component.NAVX;
import frc.robot.component.Vision;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Pneumatics;
import frc.robot.subsystem.Tilt;
import frc.robot.subsystem.elevator.Elevator;
import frc.robot.subsystem.elevator.LiftCommand;
import frc.robot.util.RioDuino;
import frc.robot.util.XboxController;

public class Robot extends TimedRobot
{
    private DriveTrain driveTrain;
    private Elevator elevator;
    private Intake intake;
    private Tilt tilt;
    private Pneumatics pneumatics;
    private Logger logger;
    private RioDuino rioDuino;
    //private CameraController cameraController;
    private ColorSensor colorSensor;
    private XboxController subsystemController;
    private PowerDistributionPanel pdp;
    private NAVX navx;
    private static Robot exposedInstance;
    private Vision visionProcessor;
    private colorAssignment colorAssignment;
    private String gameData;
    CommandGroup auto;

    @Override
    public void robotInit() {
        exposedInstance = this;

        pdp = new PowerDistributionPanel();
        subsystemController = new XboxController(Constants.RobotConstants.kController);

        pneumatics = new Pneumatics();
        driveTrain = new DriveTrain();
        elevator = new Elevator();
        intake = new Intake();
        tilt = new Tilt();

        logger = Logger.getLogger(getClass().getName());

        rioDuino = new RioDuino();

        navx = new NAVX();

        visionProcessor = new Vision();

        auto = new PracticeAuto();

        colorSensor = new ColorSensor();
        // runCamera();
        initController();
    }

    @Override
    public void robotPeriodic() {
        debug();
    }

    @Override
    public void autonomousInit() {
        auto.start();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        if (auto != null) auto.close();
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
        getColorAssignment();
        Scheduler.getInstance().run();

        // Test Code for Vision Processing, this will be moved to a command later
        if (visionProcessor.yellowBallxPos() > 0.05)
            driveTrain.tankdrive(0.7, -0.7);
        else if (visionProcessor.yellowBallxPos() < -0.05)
            driveTrain.tankdrive(-0.7, 0.7);
        else
            driveTrain.arcadeDrive(0, 0);
    }

    @Override
    public void testPeriodic() {
        new Thread() {
            @Override
            public void start() {
                while (true) {
                    System.out.println(rioDuino.receiveData());
                    for (final byte b : rioDuino.getRaw()) {
                        System.out.print(" " + (int) b);
                    }
                    try {
                        sleep(250);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void debug() {
        driveTrain.log();
        elevator.log();
        intake.log();
        tilt.log();
        visionProcessor.log();
        navx.log();
    }

    private void initController() {
        // subsystemController.Buttons.Y.runCommand(new ClimbCommand(),
        // XboxController.CommandState.WhenPressed);
        // subsystemController.Buttons.LB.runCommand(new LiftCommand(true),
        // XboxController.CommandState.WhenPressed);
        // subsystemController.Buttons.RB.runCommand(new LiftCommand(false),
        // XboxController.CommandState.WhenPressed);

        // climbing
        subsystemController.Buttons.Y.bindCommand(new ClimbCommand(), XboxController.CommandState.WhenPressed);

        // intake
        subsystemController.Buttons.A.bindCommand(new OpenCloseCommand(), XboxController.CommandState.WhenPressed);
        subsystemController.Buttons.B.bindCommand(new FollowBall(), XboxController.CommandState.WhenPressed);
        // subsystemController.Buttons.B.runCommand(new TiltCommand(),
        // XboxController.CommandState.WhenPressed);

        // elevator
        subsystemController.Buttons.LB.bindCommand(new LiftCommand(true), XboxController.CommandState.WhenPressed);
        subsystemController.Buttons.RB.bindCommand(new LiftCommand(false), XboxController.CommandState.WhenPressed);
    }

    private enum colorAssignment {
        RED, YELLOW, GREEN, BLUE, NONE
    }

    private void getColorAssignment() {
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0) {
            switch (gameData.charAt(0)) {
            case 'B':
                colorAssignment = colorAssignment.BLUE;
                break;
            case 'G':
                colorAssignment = colorAssignment.GREEN;
                break;
            case 'R':
                colorAssignment = colorAssignment.RED;
                break;
            case 'Y':
                colorAssignment = colorAssignment.YELLOW;
                break;
            default:
                // This is corrupt data
                break;
            }
        }
    }

    public void log(final Level logLevel, final String message) {
        logger.log(logLevel, message);
    }
    
    public static Robot getInstance() {
        if (exposedInstance == null) {
            throw new IllegalStateException("#robotInit must finish its invocation!");
        }
        return exposedInstance;
    }

    public Vision getVisionProcessor() {
        return visionProcessor;
    }
    
    public XboxController getSubsystemController() {
        return subsystemController;
    }

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

    public NAVX getNAVX() {
        return navx;
    }
}
