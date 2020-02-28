package frc.robot.subsystem;

import frc.robot.Robot;
import frc.robot.util.Refreshable;
import frc.robot.commands.drivetrain.DriveJoystick;
import frc.robot.commands.SetPosition;
import frc.robot.util.Loggable;
import frc.robot.util.RobotMath;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;

public class DriveTrain extends Subsystem implements Loggable, Refreshable {

    private static final double kGearRatioLow = 4.6;
    private static final double kGearRatioHigh = 2.7;

    private static final int kLeftFrontID = 12;
    private static final int kLeftBackID = 13;
    private static final int kRightFrontID = 22;
    private static final int kRightBackID = 21;

    private static final double kGearRatioFirst = 0.3;
    private static final double kGearRatioSecond = 0.4;
    private static final double kGearRatioThird = 0.5;

    // Drive Modes
    public enum DriveMode {
        AONEJOY, ATWOJOY, TANK,
    }

    // Gears (Speeds)
    public enum Gear {
        FIRST, SECOND, THIRD, FOURTH,
    }

    public DriveMode ChosenDrive;
    private Gear ChosenGear;
    private SendableChooser<DriveMode> DriveModeChooser;
    // SparkMax Objects
    private CANSparkMax leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;

    // Speed Controller Groups
    SpeedControllerGroup leftMotors, rightMotors;

    // underlying mechanism
    private DifferentialDrive drive;

    // Distance Encoders
    private CANEncoder leftFrontEncoder, rightFrontEncoder, leftBackEncoder, rightBackEncoder;

    private double distance, leftDistance, rightDistance, xPos, yPos;

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveJoystick());
    }

    public DriveTrain() {
        ChosenDrive = DriveMode.ATWOJOY;
        ChosenGear = Gear.THIRD;

        DriveModeChooser = new SendableChooser<>();
        DriveModeChooser.setDefaultOption("Arcade - One Joystick", DriveMode.AONEJOY);
        DriveModeChooser.addOption("Arcade - Two Joystick", DriveMode.ATWOJOY);
        DriveModeChooser.addOption("Tank", DriveMode.TANK);

        rightFrontMotor = new CANSparkMax(kRightFrontID, MotorType.kBrushless);
        leftFrontMotor = new CANSparkMax(kLeftFrontID, MotorType.kBrushless);
        rightBackMotor = new CANSparkMax(kRightBackID, MotorType.kBrushless);
        leftBackMotor = new CANSparkMax(kLeftBackID, MotorType.kBrushless);

        rightFrontMotor.restoreFactoryDefaults();
        leftFrontMotor.restoreFactoryDefaults();
        rightBackMotor.restoreFactoryDefaults();
        leftBackMotor.restoreFactoryDefaults();

        rightFrontMotor.setInverted(false);
        leftFrontMotor.setInverted(false);
        rightBackMotor.setInverted(false);
        leftBackMotor.setInverted(false);

        leftMotors = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
        rightMotors = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

        drive = new DifferentialDrive(leftMotors, rightMotors);

        initEncoders();

        xPos = 0;
        yPos = 0;
    }

    public void arcadeDrive(double throttle, double rotate) {
        if (ChosenGear == Gear.FIRST) {
            throttle *= kGearRatioFirst;
            rotate *= kGearRatioFirst;
        } else if (ChosenGear == Gear.SECOND) {
            throttle *= kGearRatioSecond;
            rotate *= kGearRatioSecond;
        } else if (ChosenGear == Gear.THIRD) {
            throttle *= kGearRatioThird;
            rotate *= kGearRatioThird;
        }

        SmartDashboard.putNumber("DriveTrain/Drive/ArcadeDrive/Throttle", throttle);

        SmartDashboard.putNumber("DriveTrain/Drive/ArcadeDrive/Rotate", rotate);

        drive.arcadeDrive(throttle, rotate);
    }

    public void tankdrive(double left, double right) {
        if (ChosenGear == Gear.FIRST) {
            left *= kGearRatioFirst;
            right *= kGearRatioFirst;
        } else if (ChosenGear == Gear.SECOND) {
            left *= kGearRatioSecond;
            right *= kGearRatioSecond;
        } else if (ChosenGear == Gear.THIRD) {
            left *= kGearRatioThird;
            right *= kGearRatioThird;
        }

        SmartDashboard.putNumber("DriveTrain/Drive/TankDrive/Left", left);

        SmartDashboard.putNumber("DriveTrain/Drive/TankDrive/Right", right);

        drive.tankDrive(left, right);
    }

    public void resetEncoders() {
        leftFrontEncoder.setPosition(0);
        rightFrontEncoder.setPosition(0);
        leftBackEncoder.setPosition(0);
        rightBackEncoder.setPosition(0);
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
        leftDistance = -leftFrontEncoder.getPosition();
        rightDistance = rightFrontEncoder.getPosition();
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

        xPos = xPos + (changeinDistance * Math.cos(angle));

        yPos = yPos + (changeinDistance * Math.sin(angle));
    }

    @Override
    public void log() {
        updateDistance();

        SmartDashboard.putNumber("DriveTrain/Position/Distance", distance);
        SmartDashboard.putNumber("DriveTrain/Position/xPos", getxPos());
        SmartDashboard.putNumber("DriveTrain/Position/yPos", getyPos());

        SmartDashboard.putNumber("DriveTrain/LeftEncoder/Encoder", leftDistance);
        SmartDashboard.putNumber("DriveTrain/RightEncoder/Encoder", rightDistance);

        SmartDashboard.putData("DriveTrain/Drive/Choose Drive", DriveModeChooser);

        SmartDashboard.putString("DriveTrain/Drive/Gear", ChosenGear.toString());

        SmartDashboard.putData("DriveTrain/Position/Reset Displacement", new SetPosition(0, 0));

        ChosenDrive = DriveModeChooser.getSelected();
    }

    private void initEncoders() {
        leftFrontEncoder = leftFrontMotor.getEncoder();
        leftBackEncoder = leftBackMotor.getEncoder();

        rightFrontEncoder = rightFrontMotor.getEncoder();
        rightBackEncoder = rightBackMotor.getEncoder();

        /*
         * leftLEncoder.setPositionConversionFactor(0.09); // 0.0869565217
         * rightLEncoder.setPositionConversionFactor(0.09); // 0.0869565217
         * leftFEncoder.setPositionConversionFactor(0.09); // 0.0869565217
         * rightFEncoder.setPositionConversionFactor(0.09); // 0.0869565217
         */
    }

    @Override
    public void refresh() {
    }

    public CANSparkMax getLeftFrontMotor() {
        return leftFrontMotor;
    }

    public CANSparkMax getRightFrontMotor() {
        return rightFrontMotor;
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

    public static double getKgearratiolow() {
        return kGearRatioLow;
    }

    public CANEncoder getRightFrontEncoder() {
        return rightFrontEncoder;
    }

    public void setRightFrontEncoder(CANEncoder rightFrontEncoder) {
        this.rightFrontEncoder = rightFrontEncoder;
    }

    public CANEncoder getLeftFrontEncoder() {
        return leftFrontEncoder;
    }

    public void setLeftFrontEncoder(CANEncoder leftFrontEncoder) {
        this.leftFrontEncoder = leftFrontEncoder;
    }
}
