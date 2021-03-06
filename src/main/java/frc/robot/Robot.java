package frc.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.auto.PracticeAuto;
import frc.robot.commands.drivetrain.ChangeGear;
import frc.robot.commands.drivetrain.DriveStraight;
import frc.robot.commands.intake.RunIntake;
import frc.robot.commands.shooter.ChangeShooterPower;
import frc.robot.commands.shooter.Shoot;
import frc.robot.component.ColorSensor;
import frc.robot.component.NAVX;
import frc.robot.subsystem.ControlPanelWheel;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;
import frc.robot.util.XboxController;

public class Robot extends TimedRobot {

    public static final int kSubsystemController = 1;
    public static final int kDriverController = 0;

    private DriveTrain driveTrain;
    private Intake intake;
    private Shooter shooter;
    private ControlPanelWheel controlPanelWheel;
    private Logger logger;
    private ColorSensor colorSensor;
    private XboxController subsystemController, driverController;
    private PowerDistributionPanel pdp;
    private NAVX navx;
    private static Robot exposedInstance;
    private CommandGroup auto;

    @Override
    public void robotInit() {
        exposedInstance = this;

        pdp = new PowerDistributionPanel();

        driveTrain = new DriveTrain();
        shooter = new Shooter();
        intake = new Intake();
        controlPanelWheel = new ControlPanelWheel();

        logger = Logger.getLogger(getClass().getName());

        navx = new NAVX();

        auto = new PracticeAuto();

        colorSensor = new ColorSensor();
        initControllers();
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
        Scheduler.getInstance().run();
        driveTrain.refresh();
        intake.refresh();
        shooter.refresh();
        controlPanelWheel.refresh();

        colorSensor.matchedColor();
    }

    @Override
    public void testPeriodic() {
    }

    private void debug() {
        driveTrain.log();
        intake.log();
        navx.log();
        shooter.log();
        controlPanelWheel.log();
    }

    private void initControllers() {
        subsystemController = new XboxController(kSubsystemController);
        driverController = new XboxController(kDriverController);

        // Shooter
        subsystemController.dPad.up.whenPressed(new ChangeShooterPower(true));
        subsystemController.dPad.down.whenPressed(new ChangeShooterPower(false));
        subsystemController.A.toggleWhenPressed(new Shoot());

        // Intake
        subsystemController.B.toggleWhenPressed(new RunIntake());

        // Climber

        // Drive
        driverController.RT.whenPressed(new ChangeGear(false));
        driverController.RB.whenPressed(new ChangeGear(true));
        driverController.LT.whileActive(new DriveStraight());
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

    public XboxController getDriverController() {
        return driverController;
    }

    public ControlPanelWheel getControlPanelWheel(){
        return controlPanelWheel;
    }
}