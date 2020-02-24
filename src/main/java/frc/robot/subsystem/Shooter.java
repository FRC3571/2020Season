package frc.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ManualShoot;
import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

public class Shooter extends Subsystem implements Loggable, Refreshable {
    private static final int kTopMotorID = 13;
    private static final int kBottomMotorID = 23;

    private CANSparkMax topMotor;
    private CANSparkMax bottomMotor;

    private CANEncoder topEncoder;
    private CANEncoder bottomEncoder;

    public Shooter() {
        topMotor = new CANSparkMax(kTopMotorID, MotorType.kBrushless);
        bottomMotor = new CANSparkMax(kBottomMotorID, MotorType.kBrushless);

        topMotor.restoreFactoryDefaults();
        bottomMotor.restoreFactoryDefaults();

        topMotor.setInverted(false);
        bottomMotor.setInverted(true);

        initEncoders();
    }

    @Override
    public void refresh() {
        
    }

    @Override
    public void log() {
        System.out.println("Top Speed is " + topMotor.get());
        System.out.println("Bottom Speed is " + bottomMotor.get());
    }

    public void setMotors(double topSpeed, double bottomSpeed) {
        topMotor.set(topSpeed);
        bottomMotor.set(bottomSpeed);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualShoot());
    }

    private void initEncoders() {
        topEncoder = topMotor.getEncoder();
        bottomEncoder = bottomMotor.getEncoder();
    }
}