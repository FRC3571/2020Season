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
import frc.robot.commands.LiftCommand;
import frc.robot.commands.OpenCloseCommand;
import frc.robot.commands.auto.PracticeAuto;
import frc.robot.component.ColorSensor;
import frc.robot.component.NAVX;
import frc.robot.component.Vision;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Pneumatics;
import frc.robot.subsystem.Elevator;
import frc.robot.util.XboxController;

public class Robot extends TimedRobot {

    public static final int kController = 1;

        public enum ColorAssignment {
            RED, YELLOW, GREEN, BLUE, NONE
        }
        
    private DriveTrain driveTrain;
    private Elevator elevator;
    private Intake intake;
    private Pneumatics pneumatics;
    private Logger logger;
    private ColorSensor colorSensor;
    private XboxController subsystemController;
    private PowerDistributionPanel pdp;
    private NAVX navx;
    private static Robot exposedInstance;
    private Vision visionProcessor;
    private ColorAssignment colorAssignment;
    private String gameData;
    CommandGroup auto;

    @Override
    public void robotInit() {
        exposedInstance = this;

        pdp = new PowerDistributionPanel();
        subsystemController = new XboxController(kController);

        pneumatics = new Pneumatics();
        driveTrain = new DriveTrain();
        elevator = new Elevator();
        intake = new Intake();

        logger = Logger.getLogger(getClass().getName());

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
    }

    private void debug() {
        driveTrain.log();
        elevator.log();
        intake.log();
        visionProcessor.log();
        navx.log();
    }

    private void initController() {
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

    private void getColorAssignment() {
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0) {
            switch (gameData.charAt(0)) {
            case 'B':
                colorAssignment = ColorAssignment.BLUE;
                break;
            case 'G':
                colorAssignment = ColorAssignment.GREEN;
                break;
            case 'R':
                colorAssignment = ColorAssignment.RED;
                break;
            case 'Y':
                colorAssignment = ColorAssignment.YELLOW;
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