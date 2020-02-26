package frc.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.auto.PracticeAuto;
import frc.robot.commands.shooter.ChangeShooterPower;
import frc.robot.commands.shooter.Shoot;
import frc.robot.component.ColorSensor;
import frc.robot.component.NAVX;
import frc.robot.component.Vision;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;
import frc.robot.util.XboxController;

public class Robot extends TimedRobot {

    public static final int kController = 1;

    public enum ColorAssignment {
        RED, YELLOW, GREEN, BLUE, NONE
    }

    private DriveTrain driveTrain;
    private Intake intake;
    private Shooter shooter;
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

        driveTrain = new DriveTrain();
        shooter = new Shooter();
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
        if (auto != null)
            auto.close();
    }

    @Override
    public void teleopPeriodic() {

        driveTrain.refresh();
        intake.refresh();
        shooter.refresh();

        colorSensor.matchedColor();
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
        intake.log();
        visionProcessor.log();
        navx.log();
        shooter.log();
    }

    private void initController() {
        //Shooter
        subsystemController.dPad.up.whenPressed(new ChangeShooterPower(true));
        subsystemController.dPad.down.whenPressed(new ChangeShooterPower(false));
        subsystemController.a.toggleWhenPressed(new Shoot());

        //Intake

        //Climber



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

    public Intake getIntake() {
        return intake;
    }

    public Shooter getShooter() {
        return shooter;
    }

    public PowerDistributionPanel getPowerData() {
        return pdp;
    }

    public NAVX getNAVX() {
        return navx;
    }
}