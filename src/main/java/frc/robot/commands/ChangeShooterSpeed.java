package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class ChangeShooterSpeed extends InstantCommand {

    private Shooter shooter;
    private double speed;
    private int motor;

    public ChangeShooterSpeed(int motor) {
        this.shooter = Robot.getInstance().getShooter();
        this.motor = motor;
        this.speed = shooter.getSpeedChange();
        requires(shooter);
    }

    @Override
    protected void initialize() {
        System.out.println("THIS WORKED");
        
        if (motor == 0) {
            shooter.setTopSpeed(shooter.getTopSpeed()+speed);
            shooter.setBottomSpeed(shooter.getBottomSpeed()+speed);
        } else if (motor == 1)
            shooter.setTopSpeed(shooter.getTopSpeed()+speed);
            else
            shooter.setBottomSpeed(shooter.getBottomSpeed()+speed);

        if (shooter.getTopSpeed() > 1) shooter.setTopSpeed(1);
        else if (shooter.getTopSpeed() < 0) shooter.setTopSpeed(0);

        if (shooter.getBottomSpeed() > 1) shooter.setBottomSpeed(1);
        else if (shooter.getBottomSpeed() < 0) shooter.setBottomSpeed(0);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }
}