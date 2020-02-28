package frc.robot.subsystem;

import frc.robot.util.Loggable;
import frc.robot.util.Refreshable;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem implements Loggable, Refreshable {

    private static int kMotorPort = 1;

    private VictorSPX motor;

    public Intake() {
        motor = new VictorSPX(kMotorPort);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void log() {

    }

    @Override
    protected void initDefaultCommand() {

    }

    public void setMotor(double speed) {
        motor.set(ControlMode.PercentOutput, speed);
    }
}
