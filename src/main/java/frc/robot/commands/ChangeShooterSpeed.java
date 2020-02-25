package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class ChangeShooterSpeed extends InstantCommand {

    private Shooter shooter;
    private double speed;
    private int motor;

    public ChangeShooterSpeed(int motor, double speed) {
        this.shooter = Robot.getInstance().getShooter();
        this.motor = motor;
        this.speed = speed;
        requires(shooter);
    }

    @Override
    protected void initialize() {
        if (motor == 0) {
            shooter.setTopSpeed(speed);
            shooter.setBottomSpeed(speed);
        } else if (motor == 1)
            shooter.setTopSpeed(speed);
            else
            shooter.setBottomSpeed(speed);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }
}