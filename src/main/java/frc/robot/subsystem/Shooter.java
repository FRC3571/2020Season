package frc.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

public class Shooter extends Subsystem implements Loggable, Refreshable {
    private static final int kFirstMotorID = 30;
    private static final int kSecondMotorID = 31;

    private CANSparkMax firstMotor;
    private CANSparkMax secondMotor;

    private CANEncoder firstEncoder;
    private CANEncoder secondEncoder;

    public Shooter(){

        // initialize hardware
        firstMotor = new CANSparkMax(kFirstMotorID, MotorType.kBrushless);
        secondMotor = new CANSparkMax(kSecondMotorID, MotorType.kBrushless);

        firstMotor.restoreFactoryDefaults();
        secondMotor.restoreFactoryDefaults();

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

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }

    private void initEncoders() {
        firstEncoder = firstMotor.getEncoder();
        secondEncoder = secondMotor.getEncoder();
    }

}