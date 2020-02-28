package frc.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

public class Shooter extends Subsystem implements Loggable, Refreshable {
    
    private static final int kTopMotorID = 11;
    private static final int kBottomMotorID = 21;

    private CANSparkMax topMotor, bottomMotor;

    private CANEncoder topEncoder, bottomEncoder;

    private double topSpeed, bottomSpeed, topBottomRatio;

    public Shooter() {
        topMotor = new CANSparkMax(kTopMotorID, MotorType.kBrushless);
        bottomMotor = new CANSparkMax(kBottomMotorID, MotorType.kBrushless);

        topMotor.restoreFactoryDefaults();
        bottomMotor.restoreFactoryDefaults();

        topMotor.setInverted(false);
        bottomMotor.setInverted(true);

        topBottomRatio = 0.75;
        bottomSpeed = 1;
        topSpeed = bottomSpeed * topBottomRatio;

        initEncoders();
    }

    @Override
    public void refresh() {

    }

    @Override
    public void log() {
        SmartDashboard.putNumber("Shooter/TopMotor/Speed", topSpeed);
        SmartDashboard.putNumber("Shooter/BottomMotor/Speed", bottomSpeed);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void setMotors(double topSpeed, double bottomSpeed) {
        topMotor.set(topSpeed);
        bottomMotor.set(bottomSpeed);
    }

    public void setSpeed(double bottomSpeed) {
        this.bottomSpeed = Math.max(0, Math.min(bottomSpeed, 1));
        this.topSpeed = bottomSpeed *= topBottomRatio;
    }

    private void initEncoders() {
        topEncoder = topMotor.getEncoder();
        bottomEncoder = bottomMotor.getEncoder();
    }

    public double getBottomSpeed() {
        return bottomSpeed;
    }

    public void setBottomSpeed(double bottomSpeed) {
        this.bottomSpeed = bottomSpeed;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }
}