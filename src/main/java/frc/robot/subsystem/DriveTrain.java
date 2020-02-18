package frc.robot.subsystem;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Constants.DriveConstants.DriveMode;
import frc.robot.Constants.DriveConstants.Gear;
import frc.robot.util.Refreshable;
import frc.robot.commands.ChangeGear;
import frc.robot.commands.DriveJoystick;
import frc.robot.commands.SetPosition;
import frc.robot.util.Loggable;
import frc.robot.util.RobotMath;
import frc.robot.util.XboxController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;

public class DriveTrain extends Subsystem implements Loggable, Refreshable {
    
    public DriveMode ChosenDrive;
    private Gear ChosenGear;
    private SendableChooser<DriveMode> DriveModeChooser;
    // SparkMax Objects
    private CANSparkMax leftF;
    private CANSparkMax rightF;
    private CANSparkMax leftL;
    private CANSparkMax rightL;

    // underlying mechanism
    private DifferentialDrive drive;

    // Distance Encoders
    private CANEncoder leftLEncoder;
    private CANEncoder rightLEncoder;
    private CANEncoder leftFEncoder;
    private CANEncoder rightFEncoder;

    private double distance, leftDistance, rightDistance;

    // Driver Controller
    private XboxController controller;

    private double xPos, yPos;

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveJoystick(controller));
    }

    public DriveTrain() {

        ChosenDrive = DriveMode.ATWOJOY;
        ChosenGear = Gear.THIRD;
        DriveModeChooser = new SendableChooser<>();

        // initialize hardware
        rightL = new CANSparkMax(Constants.DriveConstants.kRightLeadID, MotorType.kBrushless);
        leftL = new CANSparkMax(Constants.DriveConstants.kLeftLeadID, MotorType.kBrushless);
        rightF = new CANSparkMax(Constants.DriveConstants.kRightFollowID, MotorType.kBrushless);
        leftF = new CANSparkMax(Constants.DriveConstants.kLeftFollowID, MotorType.kBrushless);

        leftL.restoreFactoryDefaults();
        rightL.restoreFactoryDefaults();
        leftF.restoreFactoryDefaults();
        rightF.restoreFactoryDefaults();

        rightF.follow(rightL);
        leftF.follow(leftL);

        drive = new DifferentialDrive(leftL, rightL);

        initEncoders();

        leftL.setInverted(true);
        rightL.setInverted(true);
        leftF.setInverted(true);
        rightF.setInverted(true);

        arcadeDrive(0, 0);
        drive.setSafetyEnabled(false);

        xPos = 0;
        yPos = 0;

        initController();
    }

    public void arcadeDrive(double throttle, double rotate) {
        if (ChosenGear == Gear.FIRST) {
            throttle *= Constants.DriveConstants.kGearRatioFirst;
            rotate *= Constants.DriveConstants.kGearRatioFirst;
        } else if (ChosenGear == Gear.SECOND) {
            throttle *= Constants.DriveConstants.kGearRatioSecond;
            rotate *= Constants.DriveConstants.kGearRatioSecond;
        } else if (ChosenGear == Gear.THIRD) {
            throttle *= Constants.DriveConstants.kGearRatioThird;
            rotate *= Constants.DriveConstants.kGearRatioThird;
        }

        SmartDashboard.putNumber("DriveTrain/Drive/ArcadeDrive/Throttle", throttle);

        SmartDashboard.putNumber("DriveTrain/Drive/ArcadeDrive/Rotate", rotate);

        drive.arcadeDrive(throttle, rotate);
    }

    public void tankdrive(double left, double right) {
        if (ChosenGear == Gear.FIRST) {
            left *= Constants.DriveConstants.kGearRatioFirst;
            right *= Constants.DriveConstants.kGearRatioFirst;
        } else if (ChosenGear == Gear.SECOND) {
            left *= Constants.DriveConstants.kGearRatioSecond;
            right *= Constants.DriveConstants.kGearRatioSecond;
        } else if (ChosenGear == Gear.THIRD) {
            left *= Constants.DriveConstants.kGearRatioThird;
            right *= Constants.DriveConstants.kGearRatioThird;
        }

        SmartDashboard.putNumber("DriveTrain/Drive/TankDrive/Left", left);

        SmartDashboard.putNumber("DriveTrain/Drive/TankDrive/Right", right);

        drive.tankDrive(left, right);
    }

    public void reset() {
        leftLEncoder.setPosition(0);
        rightLEncoder.setPosition(0);
        setChosenGear(Gear.SECOND);
    }

    public void resetDisplacement() {
        xPos = 0;
        yPos = 0;
    }

    public double getDistance() {
        return distance;
    }

    private void updateDistance() {
        double changeinDistance = 0;
        double prevDistance = distance;
        leftDistance = -leftLEncoder.getPosition();
        rightDistance = rightLEncoder.getPosition();
        distance = (leftDistance + rightDistance) / 2;

        AHRS navx = Robot.getInstance().getNAVX().getAHRS();

        double angle = navx.getYaw();

        if (angle >= 0 && angle <= 90) {
            angle = RobotMath.mapDouble(angle, 0, 90, 90, 0);
        } else if (angle >= 90 && angle <= 180) {
            angle = RobotMath.mapDouble(angle, 90, 180, 360, 270);
        } else if (angle <= 0 && angle >= -90) {
            angle = RobotMath.mapDouble(angle, -90, 0, 180, 90);
        } else if (angle <= -90 && angle >= -180) {
            angle = RobotMath.mapDouble(angle, -180, -90, 270, 180);
        }

        changeinDistance = distance - prevDistance;

        angle = Math.toRadians(angle);

        setxPos(getxPos() + (changeinDistance * Math.cos(angle)));

        setyPos(getyPos() + (changeinDistance * Math.sin(angle)));
    }

    @Override
    public void log() {

        updateDistance();

        SmartDashboard.putNumber("DriveTrain/Position/Distance", distance);
        SmartDashboard.putNumber("DriveTrain/Position/xPos", getxPos());
        SmartDashboard.putNumber("DriveTrain/Position/yPos", getyPos());

        if (leftL.getIdleMode() == IdleMode.kCoast) {
            SmartDashboard.putString("DriveTrain/LeftEncoder/Idle Mode", "Coast");
        } else if (leftL.getIdleMode() == IdleMode.kBrake) {
            SmartDashboard.putString("DriveTrain/LeftEncoder/Idle Mode", "Brake");
        }

        if (rightL.getIdleMode() == IdleMode.kCoast) {
            SmartDashboard.putString("DriveTrain/RightEncoder/Idle Mode", "Coast");
        } else if (rightL.getIdleMode() == IdleMode.kBrake) {
            SmartDashboard.putString("DriveTrain/RightEncoder/Idle Mode", "Brake");
        }

        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Ramp Rate", leftL.getOpenLoopRampRate());
        SmartDashboard.putNumber("DriveTrain/RightEncoder/Ramp Rate", rightL.getOpenLoopRampRate());

        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Voltage", leftL.getBusVoltage());
        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Temperature", leftL.getMotorTemperature());
        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Output", leftL.getAppliedOutput());

        SmartDashboard.putNumber("DriveTrain/RightEncoder/Voltage", rightL.getBusVoltage());
        SmartDashboard.putNumber("DriveTrain/RightEncoder/Temperature", rightL.getMotorTemperature());
        SmartDashboard.putNumber("DriveTrain/RightEncoder/Output", rightL.getAppliedOutput());

        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Encoder", leftDistance);
        SmartDashboard.putNumber("DriveTrain/RightEncoder/Encoder", rightDistance);

        DriveModeChooser.setDefaultOption("Arcade - Two Joystick", DriveMode.ATWOJOY);
        DriveModeChooser.addOption("Arcade - One Joystick", DriveMode.AONEJOY);
        DriveModeChooser.addOption("Tank", DriveMode.TANK);
        SmartDashboard.putData("DriveTrain/Drive/Choose Drive", DriveModeChooser);

        SmartDashboard.putString("DriveTrain/Drive/Gear", ChosenGear.toString());

        SmartDashboard.putData("DriveTrain/Position/Reset Displacement", new SetPosition(0, 0));

        ChosenDrive = DriveModeChooser.getSelected();
    }

    private void initEncoders() {
        leftLEncoder = leftL.getEncoder();
        leftFEncoder = leftF.getEncoder();

        rightLEncoder = rightL.getEncoder();
        rightFEncoder = rightF.getEncoder();

        leftLEncoder.setPositionConversionFactor(0.09); // 0.0869565217
        rightLEncoder.setPositionConversionFactor(0.09); // 0.0869565217
        leftFEncoder.setPositionConversionFactor(0.09); // 0.0869565217
        rightFEncoder.setPositionConversionFactor(0.09); // 0.0869565217
    }

    private void initController(){
        controller = new XboxController(Constants.DriveConstants.kController);
        
        controller.Buttons.X.bindCommand(new ChangeGear(1), XboxController.CommandState.WhenPressed);
        controller.Buttons.Y.bindCommand(new ChangeGear(2), XboxController.CommandState.WhenPressed);
        controller.Buttons.B.bindCommand(new ChangeGear(3), XboxController.CommandState.WhenPressed);
        controller.Buttons.A.bindCommand(new ChangeGear(4), XboxController.CommandState.WhenPressed);
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    public CANSparkMax getLeftL() {
        return leftL;
    }

    public CANSparkMax getRightL() {
        return rightL;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public Gear getChosenGear() {
        return ChosenGear;
    }

    public void setChosenGear(Gear chosenGear) {
        this.ChosenGear = chosenGear;
    }
}
