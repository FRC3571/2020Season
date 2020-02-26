package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class ChangeShooterSpeed extends InstantCommand {

    private Shooter shooter;
    private double speedChange;
    private int motor;
    private boolean increase;

    public ChangeShooterSpeed(int motor, boolean increase) {
        this.shooter = Robot.getInstance().getShooter();
        this.motor = motor;
        this.speedChange = shooter.getSpeedChange();
        this.increase = increase;
        requires(shooter);
    }

    @Override
    protected void initialize() {
        if (!increase) speedChange*=-1;
        if (motor == 0) {
            shooter.setTopSpeed(shooter.getTopSpeed()+speedChange);
            shooter.setBottomSpeed(shooter.getBottomSpeed()+speedChange);
        } else if (motor == 1)
            shooter.setTopSpeed(shooter.getTopSpeed()+speedChange);
            else
            shooter.setBottomSpeed(shooter.getBottomSpeed()+speedChange);

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