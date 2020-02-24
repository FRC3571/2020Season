package frc.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ManualShoot;
import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

public class Shooter extends Subsystem implements Loggable, Refreshable {
    private static final int kLeftMotorID = 30;
    private static final int kRightMotorID = 31;

    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;

    private CANEncoder leftEncoder;
    private CANEncoder rightEncoder;

    public Shooter(){

        // initialize hardware
        leftMotor = new CANSparkMax(kLeftMotorID, MotorType.kBrushless);
        rightMotor = new CANSparkMax(kRightMotorID, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        rightMotor.restoreFactoryDefaults();

        leftMotor.setInverted(false);
        leftMotor.setInverted(true);

        initEncoders();
        
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void log() {
        // TODO Auto-generated method stub

    }

    public void setMotors(double speed){
        leftMotor.set(speed);
        rightMotor.set(speed);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ManualShoot());
    }

    private void initEncoders() {
        leftEncoder = leftMotor.getEncoder();
        rightEncoder = rightMotor.getEncoder();
    }

}