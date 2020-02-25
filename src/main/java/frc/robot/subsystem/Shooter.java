package frc.robot.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private double topSpeed, bottomSpeed;

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
        SmartDashboard.putNumber("Shooter/TopMotor/Speed", topMotor.get());
        SmartDashboard.putNumber("Shooter/BottomMotor/Speed", bottomMotor.get());
    }

    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand(new ManualShoot());
    }

    public void setMotors(double topSpeed, double bottomSpeed) {
        topMotor.set(topSpeed);
        bottomMotor.set(bottomSpeed);
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