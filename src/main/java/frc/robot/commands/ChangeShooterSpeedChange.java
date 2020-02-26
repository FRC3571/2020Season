package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystem.Shooter;

public class ChangeShooterSpeedChange extends InstantCommand {

    private Shooter shooter;
    private double multiplier;

    public ChangeShooterSpeedChange(int multiplier) {
        this.shooter = Robot.getInstance().getShooter();
        this.multiplier = multiplier;
        requires(shooter);
    }

    @Override
    protected void initialize() {
        shooter.setSpeedChange(shooter.getSpeedChange()*multiplier);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }
}